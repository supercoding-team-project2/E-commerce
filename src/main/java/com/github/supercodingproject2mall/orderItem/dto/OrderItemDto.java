package com.github.supercodingproject2mall.orderItem.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderItemDto {
    private Integer itemId;
    private Integer quantity;
    private Integer pricePerUnit;
    private String name;
    private String imageURL;
}
