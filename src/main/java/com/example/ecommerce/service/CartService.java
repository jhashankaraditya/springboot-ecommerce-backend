package com.example.ecommerce.service;

import com.example.ecommerce.dto.AddToCartRequestDTO;
import com.example.ecommerce.dto.CartItemDTO;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

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

        cartItemDTO.setCartItemId(cartItem.getId());
        cartItemDTO.setQuantity(cartItem.getQuantity());
        cartItemDTO.setPrice(product.getPrice());
        cartItemDTO.setProductId(product.getId());
        cartItemDTO.setProductName(product.getName());
        cartItemDTO.setTotalPrice(cartItem.getQuantity() * product.getPrice());

        return cartItemDTO;
    }

    public List<CartItemDTO> getCart(User user) {
        List<CartItem> items = cartItemRepository.findByUser(user);

        return items.stream().map(this::toDTO).toList();
    }

    private CartItemDTO toDTO(CartItem item) {
        double price = item.getProduct().getPrice();
        int quantity = item.getQuantity();

        return new CartItemDTO(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                quantity,
                price,
                price*quantity
        );
    }

    public CartItemDTO updateCart(Long id, int quantity, User user) {
        CartItem item = cartItemRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Cart not found..."));

        if (!item.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Unauthorized");
        }

        item.setQuantity(item.getQuantity() + quantity);

        return toDTO(cartItemRepository.save(item));
    }

    public void deleteItem(Long id, User user) {
        CartItem item = cartItemRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Cart not found"));

        if (!item.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Unauthorized");
        }

        cartItemRepository.delete(item);
    }

    @Transactional
    public void clearCart(User user) {
        List<CartItem> items = cartItemRepository.findByUser(user);
        cartItemRepository.deleteAll(items);
    }
}