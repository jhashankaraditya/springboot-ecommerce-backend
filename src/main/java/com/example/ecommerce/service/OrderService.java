package com.example.ecommerce.service;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.dto.OrderItemDTO;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.OrderItemRepository;
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
            throw new RuntimeException("Cart is empty!");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PLACED");
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
            OrderDTO orderDTO = new OrderDTO();

            orderDTO.setOrderId(order.getId());
            orderDTO.setStatus(order.getStatus());
            orderDTO.setOrderItems(order.getOrderItems().stream().map(this::toOrderItemDTO).toList());
            orderDTO.setCreatedAt(order.getCreatedAt());
            orderDTO.setTotalAmount(order.getTotalAmount());

            orderDTOList.add(orderDTO);
        }

        return orderDTOList;
    }
}
