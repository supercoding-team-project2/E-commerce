package com.github.supercodingproject2mall.mypage.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MypageUserInfo {
    private String email;
    private String phoneNum;
    private String address;
    private String gender;
    private String profilePictureUrl;
    private String aboutMe;
    private Integer shoppingPay;
}
