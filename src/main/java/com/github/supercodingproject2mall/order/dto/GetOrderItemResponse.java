package com.github.supercodingproject2mall.order.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetOrderItemResponse {
    private String itemUrl;
    private String itemName;
    private String itemSize;
    private Integer itemQuantity;
    private Integer itemPrice;
}
