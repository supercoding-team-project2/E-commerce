package com.github.supercodingproject2mall.auth.response;

import com.github.supercodingproject2mall.auth.enums.ResponseType;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidateTokenResponse {
    private ResponseType responseType;
    private Claims claims;
    private String message;

    public ValidateTokenResponse(ResponseType responseType, String message) {
        this.responseType = responseType;
        this.message = message;
    }
}
