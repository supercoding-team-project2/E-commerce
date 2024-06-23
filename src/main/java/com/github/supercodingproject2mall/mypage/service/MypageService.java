package com.github.supercodingproject2mall.mypage.service;

import com.github.supercodingproject2mall.auth.repository.UserRepository;
import com.github.supercodingproject2mall.cart.mapper.CartItemMapper;
import com.github.supercodingproject2mall.cart.repository.CartRepository;
import com.github.supercodingproject2mall.mypage.dto.MypageCartItemsDto;
import com.github.supercodingproject2mall.mypage.dto.MypageUserInfo;
import com.github.supercodingproject2mall.mypage.mapper.MypageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MypageService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public Optional<MypageUserInfo> findUserInfo(Integer userId){
        return userRepository.findById(userId).map(MypageMapper.INSTANCE::userEntityToMypage);
    }

    public List<MypageCartItemsDto> getCartItemsForUser(Integer userId) {
        List<Object[]> results = cartRepository.findCartDetailsByUserId(userId);
        return results.stream()
                .map(CartItemMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }
}
