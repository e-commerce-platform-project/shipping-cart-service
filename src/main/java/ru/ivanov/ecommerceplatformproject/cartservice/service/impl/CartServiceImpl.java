package ru.ivanov.ecommerceplatformproject.cartservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ivanov.ecommerceplatformproject.cartservice.client.ProductClient;
import ru.ivanov.ecommerceplatformproject.cartservice.dto.CartProductItemDto;
import ru.ivanov.ecommerceplatformproject.cartservice.exception.CartNotFoundException;
import ru.ivanov.ecommerceplatformproject.cartservice.exception.InsufficientStockException;
import ru.ivanov.ecommerceplatformproject.cartservice.model.Cart;
import ru.ivanov.ecommerceplatformproject.cartservice.model.CartItem;
import ru.ivanov.ecommerceplatformproject.cartservice.repository.CartRepository;
import ru.ivanov.ecommerceplatformproject.cartservice.service.CartItemService;
import ru.ivanov.ecommerceplatformproject.cartservice.service.CartService;
import ru.ivanov.ecommerceplatformproject.common.dto.CartProductDto;


import java.util.*;
import java.util.stream.Collectors;

import static ru.ivanov.ecommerceplatformproject.cartservice.util.MessageUtils.CART_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    @Lazy
    @Autowired
    private CartService self;
    private final CartRepository cartRepository;
    private final CartItemService cartItemService;
    private final ProductClient productClient;
    private final CacheManager cacheManager;

    private static final String CART_CACHE_NAME = "carts";
    private static final String PRODUCT_CACHE_NAME = "products";

    @Override
//    @CachePut(value = CART_CACHE_NAME, key = "#userId")
    public void createCart(UUID userId) {
        cartRepository.save(new Cart(userId));
    }

    @Override
//    @Cacheable(value = CART_CACHE_NAME, key = "#userId")
    public Cart getCart(UUID userId) {
        return cartRepository.findCartByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(CART_NOT_FOUND.formatted(userId)));
    }

    @Override
    public List<CartProductItemDto> getUserCartItems(UUID userId) {
        Cart cart = self.getCart(userId);

//        List<CartItem> cartItems = cartItemService.getAllItemsByCartId(cart.getId());
        List<CartItem> cartItems = cart.getCartItems();

        Map<UUID, Integer> productQuantities = cartItems.stream()
                .collect(Collectors.toMap(
                        CartItem::getProductId,
                        CartItem::getQuantity
                ));

        List<UUID> productsIDs = productQuantities.keySet().stream().toList();

        if (productsIDs.isEmpty()) {
            return Collections.emptyList();
        }

        Cache cache = cacheManager.getCache(PRODUCT_CACHE_NAME);

        if (cache == null) {
           return productClient.getProducts(productsIDs).stream()
                   .map(product -> new CartProductItemDto(
                           product.id(),
                           product.name(),
                           product.price(),
                           productQuantities.get(product.id()),
                           product.availableQuantity(),
                           product.mainImageURL()
                   )).toList();
        }

        List<CartProductItemDto> productsInCart = new ArrayList<>();
        List<UUID> missingIds = new ArrayList<>();

        for (UUID id : productsIDs) {
            CartProductDto cachedProduct = cache.get(id, CartProductDto.class);
            if (cachedProduct != null) {
                productsInCart.add(new CartProductItemDto(
                        cachedProduct.id(),
                        cachedProduct.name(),
                        cachedProduct.price(),
                        productQuantities.get(cachedProduct.id()),
                        cachedProduct.availableQuantity(),
                        cachedProduct.mainImageURL()
                ));
            } else {
                missingIds.add(id);
            }
        }

        if (!missingIds.isEmpty()) {
            List<CartProductDto> missingProducts = productClient.getProducts(missingIds);
            for (CartProductDto product : missingProducts) {
                productsInCart.add(new CartProductItemDto(
                        product.id(),
                        product.name(),
                        product.price(),
                        productQuantities.get(product.id()),
                        product.availableQuantity(),
                        product.mainImageURL()
                ));

                cache.put(product.id(), product);
            }
        }

        return productsInCart;
    }

    @Override
    @Transactional
    public CartProductItemDto addItemToCart(UUID userId, UUID productId) {
        Cart cart = self.getCart(userId);

        CartItem item = cartItemService.getCartItem(cart.getId(), productId);

        CartProductDto product = productClient.getProduct(productId);

        int newQuantity = (item != null) ? item.getQuantity() + 1 : 1;

        if (newQuantity > product.availableQuantity()) {
            throw new InsufficientStockException(
                    "Cannot add more than " + product.availableQuantity() + " items. " +
                    "Already in cart: " + (item != null ? item.getQuantity() : 0)
            );
        }

        if (item == null) {
            item = new CartItem(cart.getId(), productId);
        }

        item.setQuantity(newQuantity);
        cartItemService.save(item);
        return new CartProductItemDto(
                item.getProductId(),
                product.name(),
                product.price(),
                item.getQuantity(),
                product.availableQuantity(),
                product.mainImageURL()
        );
    }

    @Override
    @Transactional
    public CartProductItemDto decreaseItemQuantityInCart(UUID userId, UUID productId) {
        Cart cart = self.getCart(userId);

        CartItem item = cartItemService.getCartItemOrThrow(cart.getId(), productId);

        CartProductDto product = productClient.getProduct(productId);

        int newQuantity = item.getQuantity() - 1;

        if (newQuantity == 0) {
            cartItemService.delete(item);
            return new CartProductItemDto(
                    item.getProductId(),
                    product.name(),
                    product.price(),
                    newQuantity,
                    product.availableQuantity(),
                    product.mainImageURL());
        }

        item.setQuantity(newQuantity);
        cartItemService.save(item);
        return new CartProductItemDto(
                item.getProductId(),
                product.name(),
                product.price(),
                item.getQuantity(),
                product.availableQuantity(),
                product.mainImageURL()
        );
    }

    @Override
    @Transactional
    public void removeItemFromCart(UUID userId, UUID productId) {
        Cart cart = self.getCart(userId);
        CartItem item = cartItemService.getCartItemOrThrow(cart.getId(), productId);
        cartItemService.delete(item);
    }

    @Override
    @Transactional
    public void removeAllItemsFromCart(UUID userId) {
        Cart cart = self.getCart(userId);
        cartItemService.deleteAllItems(cart.getId());
    }

    @Override
//    @CacheEvict(value = CART_CACHE_NAME, key = "#userId")
    @Transactional
    public void deleteCartAndAllItems(UUID userId) {
        Cart cart = self.getCart(userId);
        cartItemService.deleteAllItems(cart.getId());
        cartRepository.delete(cart);
    }
}