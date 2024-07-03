package com.github.supercodingproject2mall.cartItem.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GetCartItem {
    private Integer cartItemId;
    private String itemUrl;
    private String itemName;
    private String itemSize;
    private Integer quantity;
    private Integer totalPrice;
    private Integer itemPrice;
    private List<String> optionSize;
}
