package com.example.ecommerce.dto;

import com.example.ecommerce.model.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long orderId;

    private double totalAmount;

    private OrderStatus status;

    private LocalDateTime createdAt;

    private List<OrderItemDTO> orderItems;

    public OrderDTO() {

    }
}
