package ru.ivanov.ecommerceplatformproject.cartservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CartProductItemDto(
        UUID productId,
        String name,
        BigDecimal price,
        int quantity,
        int availableQuantity,
        String mainImageURL
) {
}