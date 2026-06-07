package com.example.ecommerce.service;

import com.example.ecommerce.dto.ReviewDTO;
import com.example.ecommerce.dto.ReviewRequestDTO;
import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.Review;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    private ReviewDTO toDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();

        reviewDTO.setId(review.getId());
        reviewDTO.setRating(review.getRating());
        reviewDTO.setComment(review.getComment());
        reviewDTO.setUsername(review.getUser().getUsername());
        reviewDTO.setCreatedAt(review.getCreatedAt());

        return reviewDTO;
    }

    public ReviewDTO createReview(Long productId, ReviewRequestDTO reviewRequestDTO,
                                  User user) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new ResourceNotFoundException("Product not found"));

        reviewRepository.findByUserAndProduct(user,product).ifPresent(review -> {
            throw new BadRequestException("You already reviewed this product");
        });

        Review review = new Review();

        review.setRating(reviewRequestDTO.getRating());
        review.setComment(reviewRequestDTO.getComment());
        review.setUser(user);
        review.setProduct(product);
        review.setCreatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);

        return toDTO(savedReview);
    }

    public List<ReviewDTO> getProductReviews(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new ResourceNotFoundException("Product not found"));

        List<Review> reviewList = reviewRepository.findByProduct(product);

        List<ReviewDTO> reviewDTOList = new ArrayList<>();

        for (Review review:reviewList) {
            reviewDTOList.add(toDTO(review));
        }

        return reviewDTOList;
    }
}
