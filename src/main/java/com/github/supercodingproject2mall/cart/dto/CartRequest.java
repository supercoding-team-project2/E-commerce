package com.github.supercodingproject2mall.cart.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CartRequest {

    private Integer itemId;
    private String size;
    private Integer quantity;
}
