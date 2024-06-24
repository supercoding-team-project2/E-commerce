package com.github.supercodingproject2mall.cart.service;

import com.github.supercodingproject2mall.auth.entity.UserEntity;
import com.github.supercodingproject2mall.auth.repository.UserRepository;
import com.github.supercodingproject2mall.cart.entity.CartEntity;
import com.github.supercodingproject2mall.cart.repository.CartRepository;
import com.github.supercodingproject2mall.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public Integer findCart(Integer id) {

        //유저 조회
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("해당 ID: "+ id+"의 유저를 찾을 수 없습니다."));

        //유저의 카트가 있는지 없는지 조회 없으면 생성
        CartEntity cartEntity = cartRepository.findByUserId(id)
                .orElseGet(()-> cartRepository.save(CartEntity.builder().user(userEntity).build()));

        log.info("CartEntity - ID: {}, User: {}", cartEntity.getId(), cartEntity.getUser());

        //우저의 카트 아이디 반환
        Integer cartId = cartEntity.getId();
        return  cartId;

    }

}
