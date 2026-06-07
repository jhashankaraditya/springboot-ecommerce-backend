package com.example.ecommerce.controller;

import com.example.ecommerce.dto.AddToCartRequestDTO;
import com.example.ecommerce.dto.CartItemDTO;
import com.example.ecommerce.dto.QuantityRequestDTO;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Cart")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Add item to cart")
    @PostMapping
    public CartItemDTO addToCart(
            @Valid @RequestBody AddToCartRequestDTO requestDTO,
            @AuthenticationPrincipal User user) {

        System.out.println("INSIDE CART CONTROLLER");
        System.out.println(user);

        return cartService.addToCart(requestDTO, user);
    }

    @Operation(summary = "Get current user's cart")
    @GetMapping
    public List<CartItemDTO> getCart(@AuthenticationPrincipal User user) {
        return cartService.getCart(user);
    }

    @Operation(summary = "Update cart item quantity")
    @PutMapping("/{id}")
    public CartItemDTO updateCart(@PathVariable Long id, @RequestBody QuantityRequestDTO request,
            @AuthenticationPrincipal User user) {
        return cartService.updateCart(id,request.getQuantity(),user);
    }

    @Operation(summary = "Remove item from cart")
    @DeleteMapping("/{id}")
    public String deleteFromCart(@PathVariable Long id, @AuthenticationPrincipal User user) {
        cartService.deleteItem(id,user);
        return "Item removed.";
    }

    @Operation(summary = "Clear cart")
    @DeleteMapping("/clear")
    public String clearCart(@AuthenticationPrincipal User user) {
        cartService.clearCart(user);
        return "Cart cleared.";
    }
}