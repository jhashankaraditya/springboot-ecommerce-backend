package com.example.ecommerce.dto;

import com.example.ecommerce.model.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusRequestDTO {
    private OrderStatus status;
}
