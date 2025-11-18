package ru.ivanov.ecommerceplatformproject.cartservice.service;

import ru.ivanov.ecommerceplatformproject.cartservice.dto.CartItemDto;
import ru.ivanov.ecommerceplatformproject.cartservice.model.Cart;

import java.util.List;
import java.util.UUID;

public interface CartService {
    void createCart(UUID userId);
    List<CartItemDto> getCartItems(UUID userId);
    void changeCartItemQuantity(UUID userId, UUID productId, int quantity);
    void removeCartItem(UUID userId, UUID productId);
    void clearCart(UUID userId);
}