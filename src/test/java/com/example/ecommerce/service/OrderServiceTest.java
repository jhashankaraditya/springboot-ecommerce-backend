package com.example.ecommerce.service;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldCancelOrderSuccessfully() {
        User user = new User();
        user.setUsername("aditya");
        user.setUserId(1L);

        Order order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setStatus(OrderStatus.PLACED);
        order.setOrderItems(List.of());

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(orderRepository.save(order))
                .thenReturn(order);

        OrderDTO result = orderService.cancelOrder(1L,user);

        assertEquals(OrderStatus.CANCELLED,result.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        User user = new User();
        user.setUsername("aditya");
        user.setUserId(1L);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,()->orderService.cancelOrder(1L,user));
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotOwnOrder() {
        User correctOwner = new User();
        correctOwner.setUserId(1L);

        User wrongUser = new User();
        wrongUser.setUserId(2L);

        Order order = new Order();
        order.setId(1L);
        order.setUser(correctOwner);
        order.setStatus(OrderStatus.PLACED);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        assertThrows(
                BadRequestException.class,
                () -> orderService.cancelOrder(1L, wrongUser)
        );
    }

    @Test
    void shouldThrowExceptionWhenOrderAlreadyShipped() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("aditya");

        Order order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setOrderItems(List.of());
        order.setStatus(OrderStatus.SHIPPED);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class,()->orderService.cancelOrder(1L,user));
    }

    @Test
    void shouldThrowExceptionWhenOrderAlreadyDelivered() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("aditya");

        Order order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setOrderItems(List.of());
        order.setStatus(OrderStatus.DELIVERED);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class,()->orderService.cancelOrder(1L,user));
    }

    @Test
    void shouldThrowExceptionWhenOrderAlreadyCancelled() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("aditya");

        Order order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setOrderItems(List.of());
        order.setStatus(OrderStatus.CANCELLED);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class,()->orderService.cancelOrder(1L,user));
    }

    @Test
    void shouldThrowExceptionWhenRepositorySaveFails() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("aditya");

        Order order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setOrderItems(List.of());
        order.setStatus(OrderStatus.PLACED);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(orderRepository.save(order))
                .thenThrow(new RuntimeException("DB Error"));

        assertThrows(RuntimeException.class,()->orderService.cancelOrder(1L,user));
    }

    @Test
    void shouldPlaceOrderSuccessfully() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("aditya");

        Product product = new Product();
        product.setId(1L);
        product.setName("test_product");
        product.setPrice(1.0);

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setUser(user);
        cartItem.setQuantity(1);
        cartItem.setProduct(product);

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);

        when(cartItemRepository.findByUser(user))
                .thenReturn(cartItems);

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(1.0);
        order.setOrderItems(List.of());
        order.setStatus(OrderStatus.PLACED);

        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);

        OrderDTO orderDTO = orderService.placeOrder(user);

        assertEquals(OrderStatus.PLACED,orderDTO.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenCartIsEmpty() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("aditya");

        Product product = new Product();
        product.setId(1L);
        product.setName("test_product");
        product.setPrice(1.0);

        List<CartItem> cartItems = new ArrayList<>();

        BadRequestException ex = assertThrows(BadRequestException.class,()->orderService.placeOrder(user));
        assertEquals("Cart is empty!",ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenOrderSaveFails() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("aditya");

        Product product = new Product();
        product.setId(1L);
        product.setName("test_product");
        product.setPrice(1.0);

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setUser(user);
        cartItem.setQuantity(1);
        cartItem.setProduct(product);

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);

        when(cartItemRepository.findByUser(user))
                .thenReturn(cartItems);

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(1.0);
        order.setOrderItems(List.of());
        order.setStatus(OrderStatus.PLACED);

        when(orderRepository.save(any(Order.class)))
                .thenThrow(new RuntimeException("Cannot save to DB!"));

        RuntimeException ex = assertThrows(RuntimeException.class,()->orderService.placeOrder(user));
        assertEquals("Cannot save to DB!",ex.getMessage());
    }
}
