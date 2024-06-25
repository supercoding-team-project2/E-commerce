package com.github.supercodingproject2mall.cartItem.mapper;

import com.github.supercodingproject2mall.mypage.dto.MypageCartItemsDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface CartItemMapper{
CartItemMapper INSTANCE = Mappers.getMapper(CartItemMapper.class);

default MypageCartItemsDto toDto(Object[] objects) {
    if (objects == null || objects.length < 4) {
        return null;
    }
    String itemName = (String) objects[0];
    Integer quantity = (Integer) objects[1];
    String optionNames = (String) objects[2];
    String optionValues = (String) objects[3];

    return new MypageCartItemsDto(itemName, quantity, optionNames, optionValues);
}
}