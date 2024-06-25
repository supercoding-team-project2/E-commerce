package com.github.supercodingproject2mall.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class TokenDTO {
    private String accessToken;
    private String refreshToken;
}
