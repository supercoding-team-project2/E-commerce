package com.github.supercodingproject2mall.order.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.supercodingproject2mall.order.util.LocalDateTimeDeserializer;
import com.github.supercodingproject2mall.order.util.LocalDateTimeSerializer;
import com.github.supercodingproject2mall.orderItem.dto.OrderItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderHistoryDto {
    private Integer orderId;
    private String orderNumber;
    private Integer totalPrice;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime orderDate;
    private List<OrderItemDto> items;
}
