package com.example.ecommerce.service;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.dto.OrderItemDTO;
import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public OrderService(CartItemRepository cartItemRepository, OrderRepository orderRepository) {
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
    }

    public OrderDTO placeOrder(User user) {
        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cart is empty!");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PLACED);
        order.setCreatedAt(LocalDateTime.now());

        double totalAmount = 0;

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem: cartItems) {
            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());

            double price = cartItem.getProduct().getPrice();
            orderItem.setPrice(price);

            double itemTotalPrice = price*cartItem.getQuantity();
            orderItem.setTotalPrice(itemTotalPrice);

            totalAmount+=itemTotalPrice;

            orderItems.add(orderItem);
        }

        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        cartItemRepository.deleteAll(cartItems);

        return toDTO(savedOrder);
    }

    private OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        return new OrderItemDTO(
                orderItem.getProduct().getId(),
                orderItem.getProduct().getName(),
                orderItem.getQuantity(),
                orderItem.getPrice(),
                orderItem.getTotalPrice()
        );
    }

    private OrderDTO toDTO(Order order) {
        OrderDTO dto = new OrderDTO();

        dto.setOrderId(order.getId());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setOrderItems(order.getOrderItems().stream().map(this::toOrderItemDTO).toList());

        return dto;
    }

    public List<OrderDTO> getMyOrders(User user) {
        List<Order> orders = orderRepository.findByUser(user);

        List<OrderDTO> orderDTOList = new ArrayList<>();

        for (Order order:orders) {
            OrderDTO orderDTO = toDTO(order);

            orderDTOList.add(orderDTO);
        }

        return orderDTOList;
    }

    public OrderDTO getOrderById(Long id, User user) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "Order not found"
        ));

        User orderUser = order.getUser();

        boolean isOwner = orderUser.getUserId().equals(user.getUserId());
        boolean isAdmin = user.getRole().equals("ROLE_ADMIN");

        if (!isOwner && !isAdmin) {
            throw new BadRequestException(
                    "You cannot view this order"
            );
        }

        return toDTO(order);
    }

    public OrderDTO updateOrderStatus(Long id, OrderStatus orderStatus) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "Order not found!"
        ));

        order.setStatus(orderStatus);

        Order savedOrder = orderRepository.save(order);
        return toDTO(savedOrder);
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public OrderDTO cancelOrder(Long id, User user) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "Order not found!"
        ));

        boolean isOwner = order.getUser().getUserId().equals(user.getUserId());

        if (!isOwner) {
            throw new BadRequestException("You cannot cancel this order.");
        }

        if (order.getStatus().equals(OrderStatus.DELIVERED)) {
            throw new BadRequestException("Delivered order cannot be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);

        return toDTO(orderRepository.save(order));
    }
}
