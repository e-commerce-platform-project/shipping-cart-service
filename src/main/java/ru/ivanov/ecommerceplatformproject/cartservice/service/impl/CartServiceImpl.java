package ru.ivanov.ecommerceplatformproject.cartservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ivanov.ecommerceplatformproject.cartservice.client.ProductServiceClient;
import ru.ivanov.ecommerceplatformproject.cartservice.dto.CartItemDto;
import ru.ivanov.ecommerceplatformproject.cartservice.exception.CartNotFoundException;
import ru.ivanov.ecommerceplatformproject.cartservice.mapper.CartItemMapper;
import ru.ivanov.ecommerceplatformproject.cartservice.model.Cart;
import ru.ivanov.ecommerceplatformproject.cartservice.model.CartItem;
import ru.ivanov.ecommerceplatformproject.cartservice.repository.CartRepository;
import ru.ivanov.ecommerceplatformproject.cartservice.service.CartService;
import ru.ivanov.ecommerceplatformproject.sharedlibs.dto.CartProductDto;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductServiceClient productServiceClient;
    private final CartItemMapper cartItemMapper;
    
    @Override
    public void createCart(UUID userId) {
        cartRepository.save(new Cart(userId));
    }

    @Override
    public List<CartItemDto> getCartItems(UUID userId) {
        Cart cart = findCartByUserIdOrThrow(userId);

        List<CartItem> cartItems = cart.getCartItems();
        
        if (cartItems.isEmpty()) {
            return Collections.emptyList();
        }

//        Map<UUID, Integer> productQuantities = cartItems.stream()
//                .collect(Collectors.toMap(
//                        CartItem::getProductId,
//                        CartItem::getQuantity
//                ));
//
//        List<UUID> productsIDs = productQuantities.keySet().stream().toList();

        return cartItems.stream()
                .map(item -> {
                    CartProductDto productDto = productServiceClient.getProductById(item.getProductId());
                    return new CartItemDto(
                            item.getProductId(),
                            productDto.name(),
                            productDto.price(),
                            item.getQuantity(),
                            productDto.mainImageURL()
                    );
                })
                .toList();
        
        
        

//        Cache cache = cacheManager.getCache(PRODUCT_CACHE_NAME);

//        if (cache == null) {
//           return productServiceClient.getProducts(productsIDs).stream()
//                   .map(product -> new CartItemDto(
//                           product.id(),
//                           product.name(),
//                           product.price(),
//                           productQuantities.get(product.id()),
//                           product.availableQuantity(),
//                           product.mainImageURL()
//                   )).toList();
//        }

//        List<CartItemDto> productsInCart = new ArrayList<>();
//
//        List<UUID> missingIds = new ArrayList<>();
//
//        for (UUID id : productsIDs) {
//            CartProductDto cachedProduct = cache.get(id, CartProductDto.class);
//            if (cachedProduct != null) {
//                productsInCart.add(new CartItemDto(
//                        cachedProduct.id(),
//                        cachedProduct.name(),
//                        cachedProduct.price(),
//                        productQuantities.get(cachedProduct.id()),
//                        cachedProduct.availableQuantity(),
//                        cachedProduct.mainImageURL()
//                ));
//            } else {
//                missingIds.add(id);
//            }
//        }
//
//        if (!missingIds.isEmpty()) {
//            List<CartProductDto> missingProducts = productServiceClient.getProducts(missingIds);
//            for (CartProductDto product : missingProducts) {
//                productsInCart.add(new CartItemDto(
//                        product.id(),
//                        product.name(),
//                        product.price(),
//                        productQuantities.get(product.id()),
//                        product.availableQuantity(),
//                        product.mainImageURL()
//                ));
//
//                cache.put(product.id(), product);
//            }
//        }


    }

    @Override
    @Transactional
    public void changeCartItemQuantity(UUID userId, UUID productId, int quantity) {
        Cart cart = findCartByUserIdOrThrow(userId);
        cart.changeItemQuantity(productId, quantity);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void removeCartItem(UUID userId, UUID productId) {
        Cart cart = findCartByUserIdOrThrow(userId);
        cart.deleteItem(productId);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void clearCart(UUID userId) {
        Cart cart = findCartByUserIdOrThrow(userId);
        cart.clear();
        cartRepository.save(cart);
    }

    public Cart findCartByUserIdOrThrow(UUID userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(""));
    }
}