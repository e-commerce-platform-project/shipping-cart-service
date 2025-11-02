package ru.ivanov.ecommerceplatformproject.cartservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ecommerceplatformproject.cartservice.dto.CartProductItemDto;
import ru.ivanov.ecommerceplatformproject.cartservice.service.CartService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/carts")
public class CartRestControllerV1 {

    private final CartService cartService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CartProductItemDto> getUserCartProducts(
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return cartService.getUserCartItems(userId);
    }

    @PostMapping("/items/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public void addProductToCart( // todo думаю стоит просто возвращать 200 без дто
            @PathVariable("productId") UUID productId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        cartService.addItemToCart(userId, productId);
    }

    @PostMapping("/items/{productId}/decrement")
    @ResponseStatus(HttpStatus.OK)
    public void decrementProductQuantityInCart(
            @PathVariable("productId") UUID productId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        cartService.decreaseItemQuantityInCart(userId, productId);
    }

    @DeleteMapping("/items/{productId}")
    @ResponseStatus(HttpStatus.OK) // todo или no content
    public void removeProductFromCart(
            @PathVariable("productId") UUID productId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        cartService.removeItemFromCart(userId, productId);
    }

    @DeleteMapping("/items")
    @ResponseStatus(HttpStatus.OK) // todo или no content
//    @PreAuthorize("hasRole('USER')")
    public void removeAllItemsFromCart(
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        cartService.removeAllItemsFromCart(userId);
    }
}