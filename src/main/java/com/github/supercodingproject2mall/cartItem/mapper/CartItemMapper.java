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
    Integer price = (Integer) objects[1];
    String imageURL = (String) objects[2];
    Integer quantity = (Integer) objects[3];
    String size = (String) objects[4];

    return new MypageCartItemsDto(itemName, price, imageURL , quantity,size);
}
}