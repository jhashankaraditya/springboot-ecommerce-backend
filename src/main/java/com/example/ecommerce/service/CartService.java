package com.example.ecommerce.service;

import com.example.ecommerce.dto.AddToCartRequestDTO;
import com.example.ecommerce.dto.CartItemDTO;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartService(ProductRepository productRepository,
                       CartItemRepository cartItemRepository) {

        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public CartItemDTO addToCart(AddToCartRequestDTO requestDTO, User user) {
        Product product = productRepository.findById(requestDTO.getProductId()).orElseThrow(
                () -> new ResourceNotFoundException("Product could not be found")
        );

        CartItem cartItem = new CartItem();

        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(requestDTO.getQuantity());

        cartItemRepository.save(cartItem);

        CartItemDTO cartItemDTO = new CartItemDTO();

        cartItemDTO.setQuantity(cartItem.getQuantity());
        cartItemDTO.setPrice(product.getPrice());
        cartItemDTO.setProductId(product.getId());
        cartItemDTO.setProductName(product.getName());
        cartItemDTO.setTotalPrice(cartItem.getQuantity() * product.getPrice());

        return cartItemDTO;
    }
}