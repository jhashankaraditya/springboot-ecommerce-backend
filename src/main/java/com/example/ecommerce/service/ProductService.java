package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.dto.ProductPageResponse;
import com.example.ecommerce.dto.ProductRequestDTO;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public ProductService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();

        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setDescription(product.getDescription());
        productDTO.setImageUrl(product.getImageUrl());

        if (product.getCategory()!=null) {
            productDTO.setCategoryName(product.getCategory().getName());
        }
        else {
            productDTO.setCategoryName("No category");
        }

        return productDTO;
    }

    public ProductDTO createProduct(ProductRequestDTO productRequestDTO) {
        Category category = categoryRepository.findById(productRequestDTO.getCategoryId())
                .orElseThrow(()->new RuntimeException("Category not found"));

        Product product = new Product();
        product.setName(productRequestDTO.getName());
        product.setPrice(productRequestDTO.getPrice());
        product.setDescription(productRequestDTO.getDescription());
        product.setImageUrl(productRequestDTO.getImageUrl());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

//    public Page<ProductDTO> getProducts(int page, int size, String sortBy) {
//        Pageable pageable = PageRequest.of(page,size, Sort.by(sortBy));
//
//        Page<Product> productPage = productRepository.findAll(pageable);
//
//        return productPage.map(this::convertToDTO);
//    }

//    public ProductPageResponse getProducts(int page, int size, String sortBy) {
//        Pageable pageable = PageRequest.of(page,size, Sort.by(sortBy));
//
//        Page<Product> productPage = productRepository.findAll(pageable);
//
//        List<ProductDTO> content = productPage.getContent()
//                .stream()
//                .map(this::convertToDTO)
//                .toList();
//
//        ProductPageResponse response = new ProductPageResponse();
//        response.setContent(content);
//        response.setPage(page);
//        response.setLast(productPage.isLast());
//        response.setTotalPages(productPage.getTotalPages());
//        response.setSize(size);
//        response.setTotalElements(productPage.getTotalElements());
//
//        return response;
//    }

    public ProductPageResponse getProducts(int page, int size, String sortBy, String category,
                                           Double minPrice, Double maxPrice, String keyword) {
        List<String> allowedSortFields = List.of("id", "name", "price");

        Sort sort;

        if (allowedSortFields.contains(sortBy)) {
            sort = Sort.by(sortBy);
        } else {
            sort = Sort.by("id");
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productPage;

        if (keyword!=null && !keyword.isBlank()) {
            productPage = productRepository.findByNameContainingIgnoreCase(keyword,pageable);
        }
        else if (category!=null && minPrice!=null && maxPrice!=null) {
            productPage = productRepository.findByCategory_NameAndPriceBetween(category, minPrice,
                    maxPrice, pageable);
        }
        else if (category!=null && minPrice!=null) {
            productPage = productRepository.findByCategoryNameAndPriceGreaterThanEqual(category,
                    minPrice,pageable);
        }
        else if (category!=null && maxPrice!=null) {
            productPage = productRepository.findByCategoryNameAndPriceLessThanEqual(category,
                    maxPrice,pageable);
        }
        else if (category!=null) {
            productPage = productRepository.findByCategory_Name(category,pageable);
        }
        else if (minPrice!=null && maxPrice!=null) {
            productPage = productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
        }
        else if (minPrice!=null) {
            productPage = productRepository.findByPriceGreaterThanEqual(minPrice,pageable);
        }
        else if (maxPrice!=null) {
            productPage = productRepository.findByPriceLessThanEqual(maxPrice,pageable);
        }
        else productPage = productRepository.findAll(pageable);

        List<ProductDTO> content = productPage.getContent()
                .stream()
                .map(this::convertToDTO)
                .toList();

        ProductPageResponse response = new ProductPageResponse();
        response.setContent(content);
        response.setSize(size);
        response.setPage(page);
        response.setLast(productPage.isLast());
        response.setTotalElements(productPage.getTotalElements());
        response.setTotalPages(productPage.getTotalPages());

        return response;
    }

    public ProductDTO updateProduct(Long id, ProductRequestDTO dto) {
        Product product = productRepository.findById(id).orElseThrow(() -> new
                ResourceNotFoundException("Product not found"));

        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category not found"));

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setCategory(category);
        product.setDescription(dto.getDescription());
        product.setImageUrl(dto.getImageUrl());

        Product updatedProduct = productRepository.save(product);

        return convertToDTO(updatedProduct);

    }

    public String deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "Product not found"));

        productRepository.delete(product);

        return "Product deleted successfully!";
    }

    public List<ProductDTO> getProductsByCategory(String categoryName) {

        Pageable pageable = PageRequest.of(0,100);

        Page<Product> products = productRepository.findByCategory_Name(categoryName,pageable);

        return products.getContent().stream().map(this::convertToDTO).toList();
    }
}
