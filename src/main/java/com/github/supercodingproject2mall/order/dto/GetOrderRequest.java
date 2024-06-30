package com.github.supercodingproject2mall.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class GetOrderRequest {

    private List<Integer> cartItemId;
}
