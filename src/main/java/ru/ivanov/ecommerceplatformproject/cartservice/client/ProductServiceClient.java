package ru.ivanov.ecommerceplatformproject.cartservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.ivanov.ecommerceplatformproject.sharedlibs.dto.CartProductDto;

import java.util.List;
import java.util.UUID;

@FeignClient(
        name = "PRODUCT-SERVICE"
//        configuration = todo здесь надо добавить конфигурацию в которой будет обработка ошибок
)
public interface ProductServiceClient {

    @GetMapping("/api/v1/products/cart/{productId}")
    CartProductDto getProductById(@PathVariable("productId") UUID productId);

    @GetMapping("/api/v1/products/cart")
    List<CartProductDto> getProductsBatch(@RequestParam(name = "ids") List<UUID> productsIDs);
}