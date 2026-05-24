package com.example.ecommerce.controller;

import com.example.ecommerce.dto.AddToCartRequestDTO;
import com.example.ecommerce.dto.CartItemDTO;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public CartItemDTO addToCart(
            @Valid @RequestBody AddToCartRequestDTO requestDTO,
            @AuthenticationPrincipal User user) {

        System.out.println("INSIDE CART CONTROLLER");
        System.out.println(user);

        return cartService.addToCart(requestDTO, user);
    }
}