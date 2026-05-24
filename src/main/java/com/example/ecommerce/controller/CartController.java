package com.example.ecommerce.controller;

import com.example.ecommerce.dto.AddToCartRequestDTO;
import com.example.ecommerce.dto.CartItemDTO;
import com.example.ecommerce.dto.QuantityRequestDTO;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public List<CartItemDTO> getCart(@AuthenticationPrincipal User user) {
        return cartService.getCart(user);
    }

    @PutMapping("/{id}")
    public CartItemDTO updateCart(@PathVariable Long id, @RequestBody QuantityRequestDTO request,
            @AuthenticationPrincipal User user) {
        return cartService.updateCart(id,request.getQuantity(),user);
    }

    @DeleteMapping("/{id}")
    public String deleteFromCart(@PathVariable Long id, @AuthenticationPrincipal User user) {
        cartService.deleteItem(id,user);
        return "Item removed.";
    }

    @DeleteMapping("/clear")
    public String clearCart(@AuthenticationPrincipal User user) {
        cartService.clearCart(user);
        return "Cart cleared.";
    }
}