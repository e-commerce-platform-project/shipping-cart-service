package ru.ivanov.ecommerceplatformproject.cartservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.ivanov.ecommerceplatformproject.common.dto.CartProductDto;

import java.util.List;
import java.util.UUID;

@FeignClient(value = "PRODUCT-SERVICE")
public interface ProductFeign {

    @GetMapping("/api/v1/products/cart/{productId}")
    ResponseEntity<CartProductDto> getProductById(@PathVariable("productId") UUID productId);

    @GetMapping("/api/v1/products/cart")
    ResponseEntity<List<CartProductDto>> getProductsById(@RequestParam(name = "ids") List<UUID> productsIDs);
}