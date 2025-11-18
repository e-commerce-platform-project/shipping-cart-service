package ru.ivanov.ecommerceplatformproject.cartservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemDto(
        UUID productId,
        String name,
        BigDecimal price,
        int quantity,
        String mainImageURL
) {
}