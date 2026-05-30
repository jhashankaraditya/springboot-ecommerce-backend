package com.example.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @Positive
    private Double price;
    private String imageUrl;

    @NotNull(message = "Category is required")
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    //getters and setters
}