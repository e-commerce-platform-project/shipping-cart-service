package ru.ivanov.ecommerceplatformproject.cartservice.service;

import ru.ivanov.ecommerceplatformproject.cartservice.dto.CartProductItemDto;
import ru.ivanov.ecommerceplatformproject.cartservice.model.Cart;

import java.util.List;
import java.util.UUID;

public interface CartService {
    void createCart(UUID userId);
    Cart getCart(UUID userId);
    List<CartProductItemDto> getUserCartItems(UUID userId);
    CartProductItemDto addItemToCart(UUID userId, UUID productId);
    CartProductItemDto decreaseItemQuantityInCart(UUID userId, UUID productId);
    void removeItemFromCart(UUID userId, UUID productId);
    void removeAllItemsFromCart(UUID userId);
    void deleteCartAndAllItems(UUID userId);
}