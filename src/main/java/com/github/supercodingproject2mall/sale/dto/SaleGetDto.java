package com.github.supercodingproject2mall.sale.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.supercodingproject2mall.itemSize.dto.ItemSizeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SaleGetDto {

    private String name;
    private String description;
    private int price;
    private String categoryGender;
    private String categoryKind;
    private LocalDate listedDate;
    private LocalDate endDate;
    private Integer id;
    private String firstImageUrl;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ItemSizeDto> itemSizes;

    public SaleGetDto(String name, String description, int price, String categoryGender,
                      String categoryKind, LocalDate listedDate, LocalDate endDate, Integer id, String firstImageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryGender = categoryGender;
        this.categoryKind = categoryKind;
        this.listedDate = listedDate;
        this.endDate = endDate;
        this.id = id;
        this.firstImageUrl = firstImageUrl;
    }

    public void setSizes(List<ItemSizeDto> itemSizes) {
        if ("bag".equalsIgnoreCase(this.categoryKind)) {
            this.itemSizes = itemSizes != null ? itemSizes : new ArrayList<>();
        } else {
            this.itemSizes = itemSizes;
        }
    }
}
