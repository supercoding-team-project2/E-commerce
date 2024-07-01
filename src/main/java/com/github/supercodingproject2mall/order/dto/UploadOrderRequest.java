package com.github.supercodingproject2mall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UploadOrderRequest {
    private List<Integer> cartItemId;
    private Integer totalPrice;
}
