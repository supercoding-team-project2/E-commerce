package com.github.supercodingproject2mall.controller;

import com.github.supercodingproject2mall.auth.jwt.JwtTokenProvider;
import com.github.supercodingproject2mall.cart.dto.CartRequest;
import com.github.supercodingproject2mall.cart.dto.CartResponse;
import com.github.supercodingproject2mall.cartItem.dto.UpdateCartRequest;
import com.github.supercodingproject2mall.cart.service.CartService;
import com.github.supercodingproject2mall.cartItem.dto.CartItemResponse;
import com.github.supercodingproject2mall.cartItem.dto.GetCartItem;
import com.github.supercodingproject2mall.cartItem.service.CartItemService;
import com.github.supercodingproject2mall.order.dto.*;
import com.github.supercodingproject2mall.order.exception.InsufficientStockException;
import com.github.supercodingproject2mall.order.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final JwtTokenProvider jwtTokenProvider;
    private final OrderService orderService;

    @PostMapping("/cart/add")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<CartResponse> uploadCart(HttpServletRequest request, @RequestBody CartRequest cartRequest) {
        String token = jwtTokenProvider.resolveToken(request);
            Integer userId = jwtTokenProvider.getUserId(token);
            Integer userCartId = cartService.findCart(userId);
            cartItemService.addItemToCart(userCartId,cartRequest);
        return ResponseEntity.ok(new CartResponse("성공적으로 장바구니에 담겼습니다."));
    }

    @GetMapping("/cart")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<CartItemResponse> getCart(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        Integer userId = jwtTokenProvider.getUserId(token);
        log.info("장바구니 조회할 유저의 userId={}",userId);
        List<GetCartItem> getCartItems = cartItemService.getUserCart(userId);
        return ResponseEntity.ok(new CartItemResponse(getCartItems));
    }

    @DeleteMapping("/cart/{cartItemId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<CartResponse> deleteCart(HttpServletRequest request, @PathVariable String cartItemId) {
        String token = jwtTokenProvider.resolveToken(request);
        Integer userId = jwtTokenProvider.getUserId(token);
        cartItemService.deleteUserCartItem(userId, cartItemId);
        return ResponseEntity.ok(new CartResponse("장바구니에 담긴 해당 물품이 삭제되었습니다."));
    }

    @PutMapping("/cart/update")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<CartResponse> updateCart(HttpServletRequest request, @RequestBody UpdateCartRequest updateCartRequest){
        String token = jwtTokenProvider.resolveToken(request);
        Integer userId = jwtTokenProvider.getUserId(token);
        cartItemService.updateCart(userId,updateCartRequest);

        return ResponseEntity.ok(new CartResponse("카트 수정 완료"));
    }

    @GetMapping("/cart/order")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<GetOrderResponse> orderCart(HttpServletRequest request, @RequestParam("cartItemId") List<Integer> cartItemIds){
        String token = jwtTokenProvider.resolveToken(request);
        Integer userId = jwtTokenProvider.getUserId(token);
        return ResponseEntity.ok(orderService.getOrderCart(userId,cartItemIds));
    }

    @PostMapping("/cart/order")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> uploadOrder(HttpServletRequest request, @RequestBody UploadOrderRequest uploadOrderRequest){
        String token = jwtTokenProvider.resolveToken(request);
        Integer userId = jwtTokenProvider.getUserId(token);
        try{
            orderService.upLoadOrder(userId,uploadOrderRequest);
            return ResponseEntity.ok(new CartResponse("성공적으로 결제가 되었습니다."));
        }catch (InsufficientStockException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getInsufficientItems());
        }

    }

    @GetMapping("/cart/order/success/{orderId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<GetOrderSuccess> orderSuccess(HttpServletRequest request, @PathVariable String orderId){
        String token = jwtTokenProvider.resolveToken(request);
        Integer userId = jwtTokenProvider.getUserId(token);
        return ResponseEntity.ok(orderService.successOrder(userId,orderId));
    }
}
