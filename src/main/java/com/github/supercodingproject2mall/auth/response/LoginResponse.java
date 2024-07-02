package com.github.supercodingproject2mall.auth.response;

import com.github.supercodingproject2mall.auth.dto.TokenDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String message;
    private TokenDTO token;
    private int cartQuantity;
    private String email;

    public LoginResponse(String message) {
        this.message = message;
    }
}
