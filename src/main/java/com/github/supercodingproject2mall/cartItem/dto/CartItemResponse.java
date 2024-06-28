package com.github.supercodingproject2mall.cartItem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CartItemResponse {
    private List<GetCartItem> getCartItems;
}
