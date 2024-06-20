package com.github.supercodingproject2mall.cart.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartRequest {

    private Integer itemId;
    private Integer count;
    private Integer userId;
}
