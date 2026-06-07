package com.example.ecommerce.controller;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.dto.UpdateOrderStatusRequestDTO;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Orders")
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Place order")
    @PostMapping
    public OrderDTO placeOrder(@AuthenticationPrincipal User user) {
        return orderService.placeOrder(user);
    }

    @Operation(summary = "Get my orders")
    @GetMapping
    public List<OrderDTO> getMyOrders(@AuthenticationPrincipal User user) {
        return orderService.getMyOrders(user);
    }

    @Operation(summary = "Get order by id")
    @GetMapping("/{id}")
    public OrderDTO getOrderById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return orderService.getOrderById(id,user);
    }

    @Operation(summary = "Update order status (Admin)")
    @PatchMapping("/{id}/status")
    public OrderDTO updateOrderStatus(@PathVariable Long id, @RequestBody UpdateOrderStatusRequestDTO
                                      updateOrderStatusRequestDTO) {
        return orderService.updateOrderStatus(id,updateOrderStatusRequestDTO.getStatus());
    }

    @Operation(summary = "Get all orders (Admin)")
    @GetMapping("/admin")
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @Operation(summary = "Cancel order")
    @PatchMapping("/{id}/cancel")
    public OrderDTO cancelOrder(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return orderService.cancelOrder(id,user);
    }
}
