package ru.ivanov.ecommerceplatformproject.cartservice.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.ivanov.ecommerceplatformproject.cartservice.model.valueobjects.CartItemQuantity;

@Converter
public class CartItemQuantityConverter implements AttributeConverter<CartItemQuantity, Integer> {

    @Override
    public Integer convertToDatabaseColumn(CartItemQuantity cartItemQuantity) {
        return cartItemQuantity.quantity();
    }

    @Override
    public CartItemQuantity convertToEntityAttribute(Integer quantity) {
        return new CartItemQuantity(quantity);
    }
}
