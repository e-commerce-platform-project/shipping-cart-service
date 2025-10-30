package ru.ivanov.ecommerceplatformproject.cartservice.exception;

public class ProductServiceException extends RuntimeException {
    public ProductServiceException(String message) {
        super(message);
    }
}
