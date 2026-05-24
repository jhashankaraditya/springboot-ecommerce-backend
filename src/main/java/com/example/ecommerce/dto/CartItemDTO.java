package com.example.ecommerce.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private int productId;
    private String productName;
    private int quantity;
    private double price;
    private double totalPrice;
}
