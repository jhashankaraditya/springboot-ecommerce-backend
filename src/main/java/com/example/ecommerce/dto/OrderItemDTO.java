package com.example.ecommerce.dto;

import lombok.Data;

@Data
public class OrderItemDTO {

    private Long productId;

    private String productName;

    private int quantity;

    private double price;

    private double totalPrice;

    public OrderItemDTO() {

    }

    public OrderItemDTO(Long productId, String productName, int quantity, double price,
                        double totalPrice) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
    }
}
