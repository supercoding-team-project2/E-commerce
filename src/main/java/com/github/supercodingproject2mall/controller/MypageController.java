package com.github.supercodingproject2mall.controller;

import com.github.supercodingproject2mall.auth.jwt.JwtTokenProvider;
import com.github.supercodingproject2mall.mypage.dto.MypageApiResponse;
import com.github.supercodingproject2mall.mypage.dto.MypageCartItemsDto;
import com.github.supercodingproject2mall.mypage.dto.MypageResponse;
import com.github.supercodingproject2mall.mypage.exception.ErrorType;
import com.github.supercodingproject2mall.mypage.service.MypageService;
import com.github.supercodingproject2mall.order.dto.OrderHistoryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api")
public class MypageController {
    private final MypageService mypageService;
    private final JwtTokenProvider jwtTokenProvider;

    // 유저 정보 조회 api
    @GetMapping("/mypage/user")
    @Operation(summary = "Retrieve user information", description = "Fetches user information based on JWT token.")
    @ApiResponse(responseCode = "200", description = "User information retrieved successfully",
            content = @Content(schema = @Schema(implementation = MypageResponse.class)))
    @ApiResponse(responseCode = "401", description = "Invalid or expired token")
    public MypageApiResponse<?> findUserInfo(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Integer userId = jwtTokenProvider.getUserId(token);
            return mypageService.findUserInfo(userId)
                    .map(mypage -> MypageApiResponse.success(new MypageResponse(Collections.singletonList(mypage)), "User information retrieved successfully"))
                    .orElseGet(() -> MypageApiResponse.error("User not found", ErrorType.MEMBER_NOT_FOUND));
        } else {
            return MypageApiResponse.fail("Invalid or expired token", ErrorType.AUTHENTICATION_ERROR);
        }
    }

    @GetMapping("/mypage/cart")
    @Operation(summary = "Get user's cart items", description = "Fetches cart items for the user identified by JWT token.")
    @ApiResponse(responseCode = "200", description = "Cart items retrieved successfully",
            content = @Content(schema = @Schema(implementation = MypageCartItemsDto.class)))
    @ApiResponse(responseCode = "401", description = "Invalid or expired token")
    public MypageApiResponse<?> getUserCartItems(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Integer userId = jwtTokenProvider.getUserId(token);
            List<MypageCartItemsDto> cartItems = mypageService.getCartItemsForUser(userId);
            if (cartItems.isEmpty()) {
                return MypageApiResponse.error("No cart items found", ErrorType.NOTIFICATION_NOT_FOUND);
            }
            return MypageApiResponse.success(cartItems, "Cart items retrieved successfully");
        } else {
            return MypageApiResponse.fail("Invalid or expired token", ErrorType.AUTHENTICATION_ERROR);
        }
    }

    @GetMapping("/mypage/order")
    @Operation(summary = "Get user's order history", description = "Fetches order history for the user identified by JWT token.")
    @ApiResponse(responseCode = "200", description = "Order history retrieved successfully",
            content = @Content(schema = @Schema(implementation = OrderHistoryDto.class)))
    @ApiResponse(responseCode = "401", description = "Invalid or expired token")
    public MypageApiResponse<?> getUserOrder(HttpServletRequest request){
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return MypageApiResponse.fail("Invalid or expired token", ErrorType.AUTHENTICATION_ERROR);
        }
        Integer userId = jwtTokenProvider.getUserId(token);
        List<OrderHistoryDto> orderHistory = mypageService.getOrderHistoryForUser(userId);
        if (orderHistory.isEmpty()){
            return MypageApiResponse.error("order history not found", ErrorType.NOTIFICATION_NOT_FOUND);
        }
        return MypageApiResponse.success(orderHistory, "order history retrieved successfully");
    }
}
