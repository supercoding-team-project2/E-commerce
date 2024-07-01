package com.github.supercodingproject2mall.order.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCustomerInfo {
    private String name;
    private String phoneNumber;
    private String email;
}
