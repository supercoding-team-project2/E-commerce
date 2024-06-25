package com.github.supercodingproject2mall.mypage.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.supercodingproject2mall.auth.repository.UserRepository;
import com.github.supercodingproject2mall.cart.repository.CartRepository;
import com.github.supercodingproject2mall.cartItem.mapper.CartItemMapper;
import com.github.supercodingproject2mall.mypage.dto.MypageCartItemsDto;
import com.github.supercodingproject2mall.mypage.dto.MypageUserInfo;
import com.github.supercodingproject2mall.mypage.mapper.MypageMapper;
import com.github.supercodingproject2mall.order.dto.OrderHistoryDto;
import com.github.supercodingproject2mall.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MypageService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    public Optional<MypageUserInfo> findUserInfo(Integer userId){
        return userRepository.findById(userId).map(MypageMapper.INSTANCE::userEntityToMypage);
    }

    public List<MypageCartItemsDto> getCartItemsForUser(Integer userId) {
        List<Object[]> results = cartRepository.findCartDetailsByUserId(userId);
        return results.stream()
                .map(CartItemMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    public List<OrderHistoryDto> getOrderHistoryForUser(Integer userId) {
        String json = orderRepository.findOrdersWithItemsByUserId(userId);
        try {
            return objectMapper.readValue(json, new TypeReference<List<OrderHistoryDto>>() {});
        } catch (IOException e) {
            throw new RuntimeException("JSON parsing error", e);
        }
    }
}
