package com.github.supercodingproject2mall.order.exception;

import com.github.supercodingproject2mall.order.dto.InsufficientItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InsufficientStockException extends RuntimeException {
    private String message;
    private List<InsufficientItem> insufficientItems;
}
