package com.github.supercodingproject2mall.auth.response;

import com.github.supercodingproject2mall.auth.enums.ResponseType;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidateTokenResponse {
    private ResponseType responseType;
    private String message;
    private Claims claims;

    public ValidateTokenResponse(ResponseType responseType, Claims claims) {
        this.responseType = responseType;
        this.claims = claims;
    }

    public ValidateTokenResponse(ResponseType responseType, String message) {
        this.responseType = responseType;
        this.message = message;
    }
}
