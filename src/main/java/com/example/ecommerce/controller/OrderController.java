package com.example.ecommerce.controller;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.dto.UpdateOrderStatusRequestDTO;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public List<OrderDTO> getMyOrders(@AuthenticationPrincipal User user) {
        return orderService.getMyOrders(user);
    }

    @GetMapping("/{id}")
    public OrderDTO getOrderById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return orderService.getOrderById(id,user);
    }

    @PatchMapping("/{id}/status")
    public OrderDTO updateOrderStatus(@PathVariable Long id, @RequestBody UpdateOrderStatusRequestDTO
                                      updateOrderStatusRequestDTO) {
        return orderService.updateOrderStatus(id,updateOrderStatusRequestDTO.getStatus());
    }

    @GetMapping("/admin")
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PatchMapping("/{id}/cancel")
    public OrderDTO cancelOrder(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return orderService.cancelOrder(id,user);
    }
}
