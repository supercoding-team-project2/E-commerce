package com.github.supercodingproject2mall.auth.response;

import lombok.Getter;

@Getter
public class SignupResponse {
    private String message;

    public SignupResponse(String message) {
        this.message = message;
    }
}
