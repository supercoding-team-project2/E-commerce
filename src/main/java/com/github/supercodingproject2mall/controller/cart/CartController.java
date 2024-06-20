package com.github.supercodingproject2mall.controller.cart;

import com.github.supercodingproject2mall.cart.dto.CartRequest;
import com.github.supercodingproject2mall.cart.dto.CartResponse;
import com.github.supercodingproject2mall.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/carts")
    public ResponseEntity<CartResponse> uploadCart(@RequestBody CartRequest cartRequest) {
        String response = cartService.uploadCart(cartRequest);
        return ResponseEntity.ok(new CartResponse(response));
    }
}
