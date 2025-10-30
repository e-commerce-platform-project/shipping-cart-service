package ru.ivanov.ecommerceplatformproject.cartservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ecommerceplatformproject.cartservice.dto.CartProductItemDto;
import ru.ivanov.ecommerceplatformproject.cartservice.security.UserDetailsImpl;
import ru.ivanov.ecommerceplatformproject.cartservice.service.CartService;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/carts")
public class CartRestController {

    private final CartService cartService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CartProductItemDto>> getCartItems(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        UUID userId = userDetails.getUserId();
        List<CartProductItemDto> products = cartService.getCartItems(userId);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/items/{productId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CartProductItemDto> addProductToCart(
            @PathVariable("productId") UUID productId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        UUID userId = userDetails.getUserId();
        CartProductItemDto productInCart = cartService.addItemToCart(userId, productId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productInCart);
    }

    @PostMapping("/items/{productId}/decrement")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CartProductItemDto> decrementProductQuantityInCart(
            @PathVariable("productId") UUID productId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        UUID userId = userDetails.getUserId();
        CartProductItemDto productInCart = cartService.decreaseItemQuantityInCart(userId, productId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productInCart);
    }

    @DeleteMapping("/items/{productId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> removeProductFromCart(
            @PathVariable("productId") UUID productId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        UUID userId = userDetails.getUserId();
        cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/items")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> removeAllItemsFromCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        UUID userId = userDetails.getUserId();
        cartService.removeAllItemsFromCart(userId);
        return ResponseEntity.ok().build();
    }
}