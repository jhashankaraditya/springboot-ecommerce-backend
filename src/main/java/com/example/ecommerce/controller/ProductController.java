package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.dto.ProductPageResponse;
import com.example.ecommerce.dto.ProductRequestDTO;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductService productService;

    public ProductController(ProductRepository productRepository, CategoryRepository categoryRepository,
                             ProductService productService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productService = productService;
    }

    @PostMapping
    public ProductDTO createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("CONTROLLER AUTH: "+auth);
        System.out.println("CONTROLLER AUTHORITIES: "+auth.getAuthorities());

        return productService.createProduct(productRequestDTO);
    }

//    @GetMapping
//    public Page<ProductDTO> getProducts(@RequestParam(defaultValue = "0") int page,
//                                        @RequestParam(defaultValue = "5") int size,
//                                        @RequestParam(defaultValue = "id") String sortBy) {
//        return productService.getProducts(page,size,sortBy);
//    }

    @GetMapping
    public ProductPageResponse getProducts(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "5") int size,
                                           @RequestParam(defaultValue = "id") String sortBy,
                                           @RequestParam(required = false) String category,
                                           @RequestParam(required = false) Double minPrice,
                                           @RequestParam(required = false) Double maxPrice) {
        return productService.getProducts(page,size,sortBy,category,minPrice,maxPrice);
    }

    @PutMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable int id, @Valid @RequestBody ProductRequestDTO dto) {
        return productService.updateProduct(id,dto);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable int id) {
        return productService.deleteProduct(id);
    }

    @GetMapping("/category/{name}")
    public List<ProductDTO> getProductsByCategory(@Valid @PathVariable String name) {
        return productService.getProductsByCategory(name);
    }
}