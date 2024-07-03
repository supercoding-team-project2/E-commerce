package com.github.supercodingproject2mall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryResponseDto {
    private List<OrderHistoryDto> orders;
}
