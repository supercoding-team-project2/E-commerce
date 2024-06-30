package com.github.supercodingproject2mall.item.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ItemDetailDto {
    private String itemName;
    private Integer itemPrice;
    private String itemDescription;
    private List<String> sizeOptionsWithStock;
    private List<String> imageUrls;
}
