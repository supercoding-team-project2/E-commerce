package com.github.supercodingproject2mall.sale.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SalePutDto {

    private Integer itemId;
    private String optionSize;
    private Integer newStock;

    public SalePutDto(Integer itemId, String optionSize, Integer newStock) {
        this.itemId = itemId;
        this.optionSize = optionSize;
        this.newStock = newStock;
    }
}
