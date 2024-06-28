package com.github.supercodingproject2mall.order.dto;

import com.github.supercodingproject2mall.cartItem.entity.CartItemEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class OrderRequest {

    //private List<Integer> cartItemId;
    private String orderEmail;
    private String cardNumber;
    private Date expiryDate;
    private String cvc;
    private String shippingAddress;
    private String city;
    private String stateProvince;
    private String postalCode;

}
