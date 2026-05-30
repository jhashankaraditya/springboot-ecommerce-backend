package com.example.ecommerce.controller;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderDTO placeOrder(@AuthenticationPrincipal User user) {
        return orderService.placeOrder(user);
    }

    @GetMapping
    public List<OrderDTO> getOrder(@AuthenticationPrincipal User user) {
        return orderService.getMyOrders(user);
    }
}
