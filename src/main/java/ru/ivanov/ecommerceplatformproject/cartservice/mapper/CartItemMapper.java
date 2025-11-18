package ru.ivanov.ecommerceplatformproject.cartservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.ivanov.ecommerceplatformproject.cartservice.dto.CartItemDto;
import ru.ivanov.ecommerceplatformproject.cartservice.model.CartItem;
import ru.ivanov.ecommerceplatformproject.sharedlibs.dto.CartProductDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CartItemMapper {

    @Mapping(target = "quantity", expression = "java(item.getQuantity())")
    @Mapping(target = "productId", source = "item.productId")
    @Mapping(target = "name", source = "productDto.name")
    @Mapping(target = "price", source = "productDto.price")
    @Mapping(target = "mainImageURL", source = "productDto.mainImageURL")
    CartItemDto toDto(CartItem item, CartProductDto productDto);
}