package com.github.supercodingproject2mall.cart.service;

import com.github.supercodingproject2mall.cart.dto.CartRequest;
import com.github.supercodingproject2mall.cart.entity.CartEntity;
import com.github.supercodingproject2mall.cart.repository.CartJpaRepository;
import com.github.supercodingproject2mall.item.entity.ItemEntity;
import com.github.supercodingproject2mall.item.repository.ItemJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ItemJpaRepository itemJpaRepository;
    private final UserRepository userRepository;
    private final CartJpaRepository cartJpaRepository;

    public String uploadCart(CartRequest cartRequest) {
//        private Integer itemId;
//        private Integer count;
        //private Integer userId;

//        private Integer id;
//        private Integer count;
//        private UserEntity user;
//        private ItemEntity item;

        //itemEntity 가져오기
        Optional<ItemEntity> optionalItemEntity = itemJpaRepository.findById(cartRequest.getItemId());
        ItemEntity itemEntity = optionalItemEntity.orElseThrow(() -> new RuntimeException("Item을 찾을 수가 없습니다."));

        //userEntity 가져오기
        Optional<UserEntity> optionalUserEntity = userRepository.findById(cartRequest.getUserId());
        UserEntity userEntity = optionalUserEntity.orElseThrow(() -> new RuntimeException("user를 찾을 수가 없습니다."));

        //cartEntity에 저장
        CartEntity cartEntity = cartRequestToCartEntity(cartRequest,itemEntity,userEntity);
        cartJpaRepository.save(cartEntity);
        return "장바구니 등록";
    }

    private CartEntity cartRequestToCartEntity(CartRequest cartRequest, ItemEntity itemEntity, UserEntity userEntity) {
        return CartEntity
                .builder()
                .count(cartRequest.getCount())
                .user(userEntity)
                .item(itemEntity)
                .build();
    }
}
