package com.github.supercodingproject2mall.cartItem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UpdateCartRequest {
    private String cartItemId;
    private String itemSize;
    private Integer quantity;
}
