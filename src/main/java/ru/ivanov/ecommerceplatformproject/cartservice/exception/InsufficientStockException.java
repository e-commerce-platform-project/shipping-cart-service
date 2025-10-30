package ru.ivanov.ecommerceplatformproject.cartservice.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}