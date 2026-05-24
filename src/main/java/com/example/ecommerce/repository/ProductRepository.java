package com.example.ecommerce.repository;

import com.example.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    Page<Product> findByCategory_Name(String categoryName, Pageable pageable);

//    List<Product> findByCategory_Name(String categoryName);

    Page<Product> findByPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);

    Page<Product> findByCategory_NameAndPriceBetween(String categoryName, Double minPrice,
                                                     Double maxPrice, Pageable pageable);

    Page<Product> findByPriceGreaterThanEqual(Double minPrice, Pageable pageable);

    Page<Product> findByPriceLessThanEqual(Double maxPrice, Pageable pageable);

    Page<Product> findByCategoryNameAndPriceGreaterThanEqual(String category, Double minPrice,
                                                             Pageable pageable);

    Page<Product> findByCategoryNameAndPriceLessThanEqual(String category, Double maxPrice,
                                                          Pageable pageable);
}
