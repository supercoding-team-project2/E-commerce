package com.github.supercodingproject2mall.cart.service;

import com.github.supercodingproject2mall.auth.entity.UserEntity;
import com.github.supercodingproject2mall.auth.repository.UserRepository;
import com.github.supercodingproject2mall.cart.dto.CartRequest;
import com.github.supercodingproject2mall.cart.entity.CartEntity;
import com.github.supercodingproject2mall.cart.repository.CartRepository;
import com.github.supercodingproject2mall.item.entity.ItemEntity;
import com.github.supercodingproject2mall.item.repository.ItemJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ItemJpaRepository itemJpaRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public Integer findCart(String id) {

        Integer userId = Integer.parseInt(id);

        //유저 조회
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundException("해당 ID: "+ userId+"의 유저를 찾을 수 없습니다."));

        //유저의 카트가 있는지 없는지 조회 없으면 생성
        CartEntity cartEntity = cartRepository.findByUserId(userId)
                .orElseGet(()-> cartRepository.save(CartEntity.builder().user(userEntity).build()));

        //우저의 카트 아이디 반환
        Integer cartId = cartEntity.getId();
        return  cartId;



//        //itemEntity 가져오기
//        Optional<ItemEntity> optionalItemEntity = itemJpaRepository.findById(cartRequest.getItemId());
//        ItemEntity itemEntity = optionalItemEntity.orElseThrow(() -> new RuntimeException("Item을 찾을 수가 없습니다."));
//
//        //userEntity 가져오기
//        Optional<UserEntity> optionalUserEntity = userRepository.findById(cartRequest.getUserId());
//        UserEntity userEntity = optionalUserEntity.orElseThrow(() -> new RuntimeException("user를 찾을 수가 없습니다."));
//
//        //cartEntity에 저장
//        CartEntity cartEntity = cartRequestToCartEntity(cartRequest,itemEntity,userEntity);
//        cartRepository.save(cartEntity);
//        return "장바구니 등록";
    }

//    private CartEntity cartRequestToCartEntity(CartRequest cartRequest, ItemEntity itemEntity, UserEntity userEntity) {
//        return CartEntity
//                .builder()
//                .count(cartRequest.getCount())
//                //.user(userEntity)
//                .item(itemEntity)
//                .build();
//    }
}
