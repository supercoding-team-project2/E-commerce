package com.github.supercodingproject2mall.controller;

import com.github.supercodingproject2mall.auth.jwt.JwtTokenProvider;
import com.github.supercodingproject2mall.mypage.dto.*;
import com.github.supercodingproject2mall.mypage.exception.ErrorType;
import com.github.supercodingproject2mall.mypage.service.MypageService;
import com.github.supercodingproject2mall.mypage.service.StorageService;
import com.github.supercodingproject2mall.order.dto.OrderHistoryDto;
import com.github.supercodingproject2mall.order.dto.OrderHistoryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api")
@Slf4j
public class MypageController {
    private final MypageService mypageService;
    private final JwtTokenProvider jwtTokenProvider;
    private final StorageService storageService;

    // 유저 정보 조회 api
    @GetMapping("/mypage/user")
    @Operation(summary = "Retrieve user information", description = "Fetches user information based on JWT token.")
    @ApiResponse(responseCode = "200", description = "User information retrieved successfully",
            content = @Content(schema = @Schema(implementation = MypageResponse.class)))
    @ApiResponse(responseCode = "401", description = "Invalid or expired token")
    public MypageApiResponse<?> findUserInfo(HttpServletRequest request) {
        log.info("마이페이지 유저 정보 조회하기 api");
        String token = jwtTokenProvider.resolveToken(request);
            Integer userId = jwtTokenProvider.getUserId(token);
            return mypageService.findUserInfo(userId)
                    .map(mypage -> MypageApiResponse.success(new MypageResponse(Collections.singletonList(mypage)), "User information retrieved successfully"))
                    .orElseGet(() -> MypageApiResponse.error("User not found", ErrorType.MEMBER_NOT_FOUND));
    }

    @GetMapping("/mypage/cart")
    @Operation(summary = "Get user's cart items", description = "Fetches cart items for the user identified by JWT token.")
    @ApiResponse(responseCode = "200", description = "Cart items retrieved successfully",
            content = @Content(schema = @Schema(implementation = MypageCartItemsDto.class)))
    @ApiResponse(responseCode = "401", description = "Invalid or expired token")
    public MypageApiResponse<?> getUserCartItems(HttpServletRequest request) {
        log.info("마이페이지 유저 장바구니 아이템 조회하기 api");
        String token = jwtTokenProvider.resolveToken(request);
            Integer userId = jwtTokenProvider.getUserId(token);
            List<MypageCartItemsDto> cartItems = mypageService.getCartItemsForUser(userId);
            if (cartItems.isEmpty()) {
                return MypageApiResponse.error("No cart items found", ErrorType.NOTIFICATION_NOT_FOUND);
            }
            return MypageApiResponse.success(cartItems, "Cart items retrieved successfully");
    }

    @GetMapping("/mypage/order")
    @Operation(summary = "Get user's order history", description = "Fetches order history for the user identified by JWT token.")
    @ApiResponse(responseCode = "200", description = "Order history retrieved successfully",
            content = @Content(schema = @Schema(implementation = OrderHistoryDto.class)))
    @ApiResponse(responseCode = "401", description = "Invalid or expired token")
    public MypageApiResponse<?> getUserOrder(HttpServletRequest request){
        log.info("마이페이지 유저 주문내역 조회하기 api");
        String token = jwtTokenProvider.resolveToken(request);
        Integer userId = jwtTokenProvider.getUserId(token);
        List<OrderHistoryDto> orderHistory = mypageService.getOrderHistoryForUser(userId);
        OrderHistoryResponseDto orderHistoryResponseDto = new OrderHistoryResponseDto(orderHistory);
        if (orderHistory.isEmpty()){
            return MypageApiResponse.error("order history not found", ErrorType.NOTIFICATION_NOT_FOUND);
        }
        return MypageApiResponse.success(orderHistoryResponseDto, "order history retrieved successfully");
    }

    @PutMapping("/mypage/user")
    public MypageApiResponse<?> updateUserInfo(HttpServletRequest request , @ModelAttribute MypageUserInfoUpdateDto updateDto){
        String token = jwtTokenProvider.resolveToken(request);
        Integer userId = jwtTokenProvider.getUserId(token);
        MultipartFile profileImage = updateDto.getProfileImage();
        String imageUrl = null;
        try {
            if (profileImage != null && !profileImage.isEmpty()) {
                imageUrl = storageService.uploadFile(profileImage);
            }
            MypageUserInfo updateUserInfo = mypageService.updateUserInfo(userId , updateDto , imageUrl);
            return MypageApiResponse.success(updateUserInfo,"프로필이 성공적으로 업데이트되었습니다!") ;

        } catch (IOException e) {
            return MypageApiResponse.fail("S3 or database fileUpload fail",ErrorType.SYSTEM_ERROR);
        }
    }

    @PutMapping("/mypage/recharge")
    public MypageApiResponse<?> reChargeShoppingPay(HttpServletRequest request , @RequestBody MypageRechargeShoppingPay rechargeShoppingPay){
        String token = jwtTokenProvider.resolveToken(request);
        Integer userId = jwtTokenProvider.getUserId(token);
        try {
            MypageUserInfo updateUserInfo = mypageService.rechargeShoppingPay(userId , rechargeShoppingPay);
            return MypageApiResponse.success(updateUserInfo,"페이 충전이 성공적으로 완료되었습니다!") ;
        }catch (IllegalArgumentException e){
            return MypageApiResponse.fail("User not found", ErrorType.MEMBER_NOT_FOUND);
        }
    }
}
