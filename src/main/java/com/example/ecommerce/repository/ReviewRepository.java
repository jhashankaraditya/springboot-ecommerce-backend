package com.example.ecommerce.repository;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.Review;
import com.example.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    Optional<Review> findByUserAndProduct(User user, Product product);
    List<Review> findByProduct(Product product);
}
