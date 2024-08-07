package com.github.supercodingproject2mall.auth.exception;

import lombok.Getter;

@Getter
public enum ErrorType {
    NULL_TOKEN("TOKEN IS NULL"),
    INVALID_TOKEN_SIGNATURE("잘못된 JWT 서명입니다."),
    UNSUPPORTED_TOKEN("지원되지 않는 JWT 토큰입니다."),
    INVALID_TOKEN("잘못된 JWT 토큰입니다."),
    EXPIRED_TOKEN("유효기간이 지난 토큰입니다"),

    DUPLICATE_USER("이미 존재하는 회원입니다."),
    EMAIL_NOT_EXISTS("존재하지 않는 회원입니다."),
    PASSWORD_NOT_CORRECT("비밀번호를 잘못입력했습니다."),
    DELETED_USER("탈퇴한 회원입니다."),

    SYSTEM_ERROR;

    private String message;

    ErrorType() {
        this.message = "";
    }

    ErrorType(String message) {
        this.message = message;
    }
}
