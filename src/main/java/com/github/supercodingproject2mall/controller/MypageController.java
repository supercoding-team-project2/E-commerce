package com.github.supercodingproject2mall.controller;

import com.github.supercodingproject2mall.auth.jwt.JwtTokenProvider;
import com.github.supercodingproject2mall.mypage.dto.ApiResponse;
import com.github.supercodingproject2mall.mypage.dto.MypageCartItemsDto;
import com.github.supercodingproject2mall.mypage.dto.MypageResponse;
import com.github.supercodingproject2mall.mypage.exception.ErrorType;
import com.github.supercodingproject2mall.mypage.service.MypageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MypageController {
    private final MypageService mypageService;
    private final JwtTokenProvider jwtTokenProvider;

    // 유저 정보 조회 api
    @GetMapping("/mypage/user")
    public ApiResponse<?> findUserInfo(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Integer userId = jwtTokenProvider.getUserId(token);
            return mypageService.findUserInfo(userId)
                    .map(mypage -> ApiResponse.success(new MypageResponse(Collections.singletonList(mypage)), "User information retrieved successfully"))
                    .orElseGet(() -> ApiResponse.error("User not found", ErrorType.MEMBER_NOT_FOUND));
        } else {
            return ApiResponse.fail("Invalid or expired token", ErrorType.AUTHENTICATION_ERROR);
        }
    }

    @GetMapping("/mypage/cart")
    public ApiResponse<?> getUserCartItems(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Integer userId = jwtTokenProvider.getUserId(token);
            List<MypageCartItemsDto> cartItems = mypageService.getCartItemsForUser(userId);
            if (cartItems.isEmpty()) {
                return ApiResponse.error("No cart items found", ErrorType.NOTIFICATION_NOT_FOUND);
            }
            return ApiResponse.success(cartItems, "Cart items retrieved successfully");
        } else {
            return ApiResponse.fail("Invalid or expired token", ErrorType.AUTHENTICATION_ERROR);
        }
    }
}
