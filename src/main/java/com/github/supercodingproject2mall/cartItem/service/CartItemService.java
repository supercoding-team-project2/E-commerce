package com.github.supercodingproject2mall.cartItem.service;

import com.github.supercodingproject2mall.cart.dto.CartRequest;
import com.github.supercodingproject2mall.cart.entity.CartEntity;
import com.github.supercodingproject2mall.cart.repository.CartRepository;
import com.github.supercodingproject2mall.cartItem.entity.CartItemEntity;
import com.github.supercodingproject2mall.cartItem.repository.CartItemRepository;
import com.github.supercodingproject2mall.item.entity.ItemEntity;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;


    public void addItemToCart(Integer userCartId, CartRequest cartRequest) {

//        private Integer itemId;
//        private Integer itemOptionId;
//        private Integer optionValueId;
//        private Integer quantity;

        //cartItemEntity
//        private Integer id;
//        private CartEntity cart;
//        private ItemEntity item;
//        private Integer quantity;

        //cartItemEntity형태로 바꿔주기
        CartItemEntity cartItemEntity = cartRequestToCartItemEntity(userCartId,cartRequest);

        //cartItem에 저장
        cartItemRepository.save()
    }

    private CartItemEntity cartRequestToCartItemEntity(Integer userCartId, CartRequest cartRequest) {
        CartEntity cartEntity = cartRepository.findById(userCartId)
                .orElseThrow(()-> new NotFoundException("해당 ID: "+ userCartId+"의 카트를 찾을 수 없습니다."));;

        return CartItemEntity
                .builder()
                .cart(cartEntity)
                .item()
    }
}
