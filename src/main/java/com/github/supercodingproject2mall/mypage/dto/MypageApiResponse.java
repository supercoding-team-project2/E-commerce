package com.github.supercodingproject2mall.mypage.dto;

import com.github.supercodingproject2mall.mypage.enums.ResponseType;
import com.github.supercodingproject2mall.mypage.exception.ErrorType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MypageApiResponse<T> {
    private ResponseType responseType;
    private String message;
    private T data;

    public static MypageApiResponse<Object> success() {
        return MypageApiResponse.builder().responseType(ResponseType.SUCCESS).build();
    }

    public static <T> MypageApiResponse<T> success(T data, String message) {
        return MypageApiResponse.<T>builder()
                .data(data)
                .message(message)
                .responseType(ResponseType.SUCCESS)
                .build();
    }

    public static MypageApiResponse<Object> fail(ErrorType errorType) {
        return MypageApiResponse.builder()
                .message(errorType.getMessage())
                .responseType(ResponseType.FAIL)
                .build();
    }

    public static <T> MypageApiResponse<T> fail(T data, ErrorType errorType) {
        return MypageApiResponse.<T>builder()
                .message(errorType.getMessage())
                .data(data)
                .responseType(ResponseType.FAIL)
                .build();
    }

    public static MypageApiResponse<Object> error(ErrorType errorType) {
        return MypageApiResponse.builder()
                .message(errorType.getMessage())
                .responseType(ResponseType.ERROR)
                .build();
    }

    public static <T> MypageApiResponse<T> error(T data, ErrorType errorType) {
        return MypageApiResponse.<T>builder()
                .message(errorType.getMessage())
                .data(data)
                .responseType(ResponseType.ERROR)
                .build();
    }

    public static MypageApiResponse<MypageResponse> error(String message, ErrorType errorType) {
        return MypageApiResponse.<MypageResponse>builder()
                .message(message)
                .data(null) // MypageResponse 타입의 데이터가 없으므로 null 처리
                .responseType(ResponseType.ERROR)
                .build();
    }
}