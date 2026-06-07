package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ReviewDTO;
import com.example.ecommerce.dto.ReviewRequestDTO;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{productId}/reviews")
    public ReviewDTO createReview(@PathVariable Long productId, @Valid @RequestBody ReviewRequestDTO
                                  reviewRequestDTO, @AuthenticationPrincipal User user) {
        return reviewService.createReview(productId,reviewRequestDTO,user);
    }

    @GetMapping("/{productId}/reviews")
    public List<ReviewDTO> getProductReviews(@PathVariable Long productId) {
        return reviewService.getProductReviews(productId);
    }
}
