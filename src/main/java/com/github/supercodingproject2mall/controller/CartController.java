package com.github.supercodingproject2mall.controller;

import com.github.supercodingproject2mall.auth.jwt.JwtTokenProvider;
import com.github.supercodingproject2mall.cart.dto.CartRequest;
import com.github.supercodingproject2mall.cart.dto.CartResponse;
import com.github.supercodingproject2mall.cart.dto.UpdateCartRequest;
import com.github.supercodingproject2mall.cart.service.CartService;
import com.github.supercodingproject2mall.cartItem.service.CartItemService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/cart/add")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<CartResponse> uploadCart(HttpServletRequest request, @RequestBody CartRequest cartRequest) {
        String token = jwtTokenProvider.resolveToken(request);
            Integer userId = jwtTokenProvider.getUserId(token);
            Integer userCartId = cartService.findCart(userId);
            cartItemService.addItemToCart(userCartId,cartRequest);
        return ResponseEntity.ok(new CartResponse("성공적으로 장바구니에 담겼습니다."));
    }



    @PutMapping("/cart/update")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<CartResponse> updateCart(HttpServletRequest request, @RequestBody UpdateCartRequest updateCartRequest){
        String token = jwtTokenProvider.resolveToken(request);
            cartItemService.updateCart(updateCartRequest);

        return ResponseEntity.ok(new CartResponse("카트 수정 완료"));
    }

}
