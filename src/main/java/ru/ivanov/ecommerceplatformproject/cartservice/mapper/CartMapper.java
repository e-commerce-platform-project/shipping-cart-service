package ru.ivanov.ecommerceplatformproject.cartservice.mapper;

import org.springframework.stereotype.Service;
import ru.ivanov.ecommerceplatformproject.cartservice.dto.CartDto;
import ru.ivanov.ecommerceplatformproject.cartservice.model.Cart;

@Service
public class CartMapper {

    public CartDto toDto(Cart cart) {
        return new CartDto(cart.getId(), cart.getUserId());
    }
}
