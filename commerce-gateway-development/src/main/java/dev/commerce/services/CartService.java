package dev.commerce.services;

import dev.commerce.dtos.response.CartResponse;

import java.util.UUID;

public interface CartService {
    CartResponse addToCart(UUID productId, int quantity);
    CartResponse updateQuantity(UUID cartItemId, int newQty);
    CartResponse removeCartItem(UUID cartItemId);
    CartResponse getMyCart();
    void clearCart();
}
