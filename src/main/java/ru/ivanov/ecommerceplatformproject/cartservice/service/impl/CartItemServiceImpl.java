package ru.ivanov.ecommerceplatformproject.cartservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ivanov.ecommerceplatformproject.cartservice.exception.ProductNotFoundInCartException;
import ru.ivanov.ecommerceplatformproject.cartservice.model.CartItem;
import ru.ivanov.ecommerceplatformproject.cartservice.repository.CartItemRepository;
import ru.ivanov.ecommerceplatformproject.cartservice.service.CartItemService;

import java.util.List;
import java.util.UUID;

import static ru.ivanov.ecommerceplatformproject.cartservice.util.MessageUtils.PRODUCT_NOT_FOUND_IN_CART;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public CartItem save(CartItem item) {
        return cartItemRepository.save(item);
    }

    @Override
    @Transactional(readOnly = true)
    public CartItem getCartItem(UUID cartId, UUID productId) {
        return cartItemRepository.findByCartIdAndProductId(cartId, productId)
                .orElse(null);
    }

    @Override
    @Transactional
    public CartItem getCartItemOrThrow(UUID cartId, UUID productId) {
        return cartItemRepository.findByCartIdAndProductId(cartId, productId)
                .orElseThrow(() -> new ProductNotFoundInCartException(PRODUCT_NOT_FOUND_IN_CART.formatted(productId, cartId)));
    }

    @Override
    public List<CartItem> getAllItemsByCartId(UUID cartId) {
        return cartItemRepository.findAllByCartId(cartId);
    }

    @Override
    @Transactional
    public void deleteByCartIdAndProductId(UUID cartId, UUID productId) {
        cartItemRepository.deleteByCartIdAndProductId(cartId, productId);
    }

    @Override
    @Transactional
    public void delete(CartItem item) {
        cartItemRepository.delete(item);
    }

    @Override
    @Transactional
    public void deleteAllItems(UUID cartId) {
        cartItemRepository.deleteAllByCartId(cartId);
    }
}