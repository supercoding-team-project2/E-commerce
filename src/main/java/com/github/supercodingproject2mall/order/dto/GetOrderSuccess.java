package com.github.supercodingproject2mall.order.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetOrderSuccess {
    private String orderNumber;
    private LocalDateTime orderDate;
    private Integer totalPrice;
    private OrderCustomerInfo customerInfo;
}
