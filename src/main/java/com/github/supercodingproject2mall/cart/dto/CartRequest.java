package com.github.supercodingproject2mall.cart.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CartRequest {

    private Integer userId;
    private Integer itemId;
    private List<Integer> itemOptionId;
    private List<Integer> optionValueId;
    private Integer quantity;
}
