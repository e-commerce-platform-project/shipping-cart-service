package ru.ivanov.ecommerceplatformproject.cartservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ecommerceplatformproject.cartservice.dto.CartItemDto;
import ru.ivanov.ecommerceplatformproject.cartservice.service.CartService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/carts")
public class CartRestControllerV1 {

    private final CartService cartService;

    @GetMapping
    public List<CartItemDto> getCartItems(
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return cartService.getCartItems(userId);
    }

    @PostMapping("/items/{productId}")
    public void changeCartItemQuantity(
            @PathVariable UUID productId,
            @RequestParam int quantity,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        cartService.changeCartItemQuantity(userId, productId, quantity);
    }

    @DeleteMapping("/items/{productId}")
    public void removeCartItem(
            @PathVariable UUID productId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        cartService.removeCartItem(userId, productId);
    }

    @DeleteMapping
    public void clearCart(
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        cartService.clearCart(userId);
    }
}