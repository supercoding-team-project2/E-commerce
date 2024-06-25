package com.github.supercodingproject2mall.controller;

import com.github.supercodingproject2mall.auth.jwt.JwtTokenProvider;
import com.github.supercodingproject2mall.cart.dto.CartRequest;
import com.github.supercodingproject2mall.cart.dto.CartResponse;
import com.github.supercodingproject2mall.cart.dto.UpdateCartRequest;
import com.github.supercodingproject2mall.cart.service.CartService;
import com.github.supercodingproject2mall.cartItem.service.CartItemService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;

@Slf4j
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/add")
    public ResponseEntity<CartResponse> uploadCart(HttpServletRequest request, @RequestBody CartRequest cartRequest) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)){
            Integer userId = jwtTokenProvider.getUserId(token);
            Integer userCartId = cartService.findCart(userId);
            cartItemService.addItemToCart(userCartId,cartRequest);
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CartResponse("Token이 유효하지 않거나 찾을 수 없습니다."));
        }
        return ResponseEntity.ok(new CartResponse("카트 담기 완료"));
    }

    @PutMapping("/update")
    public ResponseEntity<CartResponse> updateCart(HttpServletRequest request, @RequestBody UpdateCartRequest updateCartRequest){
        String token = jwtTokenProvider.resolveToken(request);
        if(token != null && jwtTokenProvider.validateToken(token)){
            cartItemService.updateCart(updateCartRequest);
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CartResponse("Token이 유효하지 않거나 찾을 수 없습니다."));
        }
        return ResponseEntity.ok(new CartResponse("카트 수정 완료"));
    }
}
