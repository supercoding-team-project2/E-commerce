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
    private Integer newPrice;
    private String categoryGender;

    public SalePutDto(Integer itemId, String optionSize, Integer newStock, Integer newPrice, String categoryGender) {
        this.itemId = itemId;
        this.optionSize = optionSize;
        this.newStock = newStock;
        this.newPrice = newPrice;
        this.categoryGender = categoryGender;
    }
}
