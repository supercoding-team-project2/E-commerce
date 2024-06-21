package com.github.supercodingproject2mall.mypage.dto;

import com.github.supercodingproject2mall.mypage.enums.ResponseType;
import com.github.supercodingproject2mall.mypage.exception.ErrorType;
import lombok.Builder;
import lombok.Getter;



@Getter
@Builder
public class ApiResponse<T> {
    private ResponseType responseType;
    private String message;
    private T data;

    public static ApiResponse<Object> success() {
        return ApiResponse.builder().responseType(ResponseType.SUCCESS).build();
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .data(data)
                .responseType(ResponseType.SUCCESS)
                .build();
    }

    public static ApiResponse<Object> fail(ErrorType errorType) {
        return ApiResponse.builder()
                .message(errorType.getMessage())
                .responseType(ResponseType.FAIL)
                .build();
    }

    public static <T> ApiResponse<T> fail(T data, ErrorType errorType) {
        return ApiResponse.<T>builder()
                .message(errorType.getMessage())
                .data(data)
                .responseType(ResponseType.FAIL)
                .build();
    }

    public static ApiResponse<Object> error(ErrorType errorType) {
        return ApiResponse.builder()
                .message(errorType.getMessage())
                .responseType(ResponseType.ERROR)
                .build();
    }

    public static <T> ApiResponse<T> error(T data, ErrorType errorType) {
        return ApiResponse.<T>builder()
                .message(errorType.getMessage())
                .data(data)
                .responseType(ResponseType.ERROR)
                .build();
    }
}
