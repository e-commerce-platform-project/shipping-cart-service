package ru.ivanov.ecommerceplatformproject.cartservice.model.valueobjects;

public record CartItemQuantity(
        int quantity
) {
    public CartItemQuantity {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (quantity > MAX_QUANTITY) {
            throw new IllegalArgumentException("Quantity cannot exceed " + MAX_QUANTITY);
        }
    }

    private static final int MAX_QUANTITY = 100;
}