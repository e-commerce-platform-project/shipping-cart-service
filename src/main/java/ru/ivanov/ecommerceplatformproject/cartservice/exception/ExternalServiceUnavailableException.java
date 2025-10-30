package ru.ivanov.ecommerceplatformproject.cartservice.exception;


public class ExternalServiceUnavailableException extends RuntimeException {
    public ExternalServiceUnavailableException(String message) {
        super(message);
    }
}
