package com.github.supercodingproject2mall.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UpdateCartRequest {
    private String cartItemId;
    private List<Integer> optionValueId;
    private Integer quantity;
}
