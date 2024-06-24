package com.github.supercodingproject2mall.controller;

import com.github.supercodingproject2mall.cart.dto.CartRequest;
import com.github.supercodingproject2mall.cart.dto.CartResponse;
import com.github.supercodingproject2mall.cart.service.CartService;
import com.github.supercodingproject2mall.cartItem.service.CartItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;

    @PostMapping("/add")
    public ResponseEntity<CartResponse> uploadCart(@RequestBody CartRequest cartRequest) {
        //유저 카트 없으면 생성 있으면 카트 아이디 가져옴
        log.info("CartEntity - userId: {}, itemId: {}, itemOptionId: {}, optionValueId: {}, quantity: {}",
                cartRequest.getUserId(), cartRequest.getItemId(),cartRequest.getItemOptionId(),cartRequest.getOptionValueId(),cartRequest.getQuantity());
        Integer userCartId = cartService.findCart(cartRequest.getUserId());

        //유저의 카트 아이템 목록에 값을 저장
        cartItemService.addItemToCart(userCartId,cartRequest);

        return ResponseEntity.ok(new CartResponse("카트 담기 완료"));
    }
}
