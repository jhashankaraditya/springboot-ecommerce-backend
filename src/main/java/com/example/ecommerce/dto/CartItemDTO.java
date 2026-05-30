package com.example.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private Long cartItemId;
    private Long productId;
    private String productName;
    private int quantity;
    private double price;
    private double totalPrice;
}
