package com.example.ecommerce.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductPageResponse {
    private List<ProductDTO> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
