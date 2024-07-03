package com.github.supercodingproject2mall.itemSize.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class ItemSizeDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String size;

    private int stock;

    public ItemSizeDto(String size, int stock) {
        this.size = size;
        this.stock = stock;
    }
}
