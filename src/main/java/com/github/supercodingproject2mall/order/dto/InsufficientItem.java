package com.github.supercodingproject2mall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InsufficientItem {
    private String item;
    private int quantity;
}
