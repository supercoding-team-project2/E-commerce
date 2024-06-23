package com.github.supercodingproject2mall.controller;

import com.github.supercodingproject2mall.cart.dto.CartRequest;
import com.github.supercodingproject2mall.cart.dto.CartResponse;
import com.github.supercodingproject2mall.cart.service.CartService;
import com.github.supercodingproject2mall.cartItem.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;

    @PostMapping("/add/{id}")
    public ResponseEntity<CartResponse> uploadCart(@PathVariable String id, @RequestBody CartRequest cartRequest) {
        //유저 카트 없으면 생성 있으면 카트 아이디 가져옴
        Integer userCartId = cartService.findCart(id);

        //유저의 카트 아이템 목록에 값을 저장
        cartItemService.addItemToCart(userCartId,cartRequest);

        return null;
//        return ResponseEntity.ok(new CartResponse(response));
    }
}
