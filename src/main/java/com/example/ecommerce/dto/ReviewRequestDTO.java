package com.example.ecommerce.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ReviewRequestDTO {
    @Min(1)
    @Max(5)
    private Integer rating;

    private String comment;
}
