package com.example.ecommerce.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long orderId;

    private double totalAmount;

    private String status;

    private LocalDateTime createdAt;

    private List<OrderItemDTO> orderItems;

    public OrderDTO() {

    }
}
