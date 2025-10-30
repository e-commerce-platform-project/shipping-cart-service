package ru.ivanov.ecommerceplatformproject.cartservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.ivanov.ecommerceplatformproject.cartservice.feign.ProductFeign;
import ru.ivanov.ecommerceplatformproject.common.dto.CartProductDto;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductClient {
    private static final String PRODUCT_CACHE_NAME = "products";
    private final ProductFeign productFeign;

    public List<CartProductDto> getProducts(List<UUID> productsIDs) {
        ResponseEntity<List<CartProductDto>> response = productFeign.getProductsById(productsIDs);
        return response.getBody();
    }

    @Cacheable(value = PRODUCT_CACHE_NAME, key = "#productId")
    public CartProductDto getProduct(UUID productId) {
        ResponseEntity<CartProductDto> response = productFeign.getProductById(productId);
        return response.getBody();
    }
}