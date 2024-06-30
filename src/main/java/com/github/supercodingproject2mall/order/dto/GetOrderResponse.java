package com.github.supercodingproject2mall.order.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetOrderResponse {
    private String username;
    private String phoneNumber;
    private String email;
    private String address;
    private List<GetOrderItemResponse> orderItems;
}
