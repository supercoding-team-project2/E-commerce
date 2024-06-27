package com.github.supercodingproject2mall.mypage.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.supercodingproject2mall.auth.entity.UserEntity;
import com.github.supercodingproject2mall.auth.repository.UserRepository;
import com.github.supercodingproject2mall.cart.repository.CartRepository;
import com.github.supercodingproject2mall.cartItem.entity.CartItemEntity;
import com.github.supercodingproject2mall.cartItem.mapper.CartItemMapper;
import com.github.supercodingproject2mall.mypage.dto.MypageCartItemsDto;
import com.github.supercodingproject2mall.mypage.dto.MypageUserInfo;
import com.github.supercodingproject2mall.mypage.mapper.MypageMapper;
import com.github.supercodingproject2mall.order.dto.OrderHistoryDto;
import com.github.supercodingproject2mall.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class MypageServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private MypageService mypageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void findUserInfo() throws Exception{
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setEmail("example@example.com");
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));
        when(MypageMapper.INSTANCE.userEntityToMypage(any())).thenReturn(new MypageUserInfo());

        Optional<MypageUserInfo> result = mypageService.findUserInfo(1);

        assertTrue(result.isPresent());
        verify(userRepository).findById(1);
    }

    @Test
    void getCartItemsForUser() throws Exception{
        Object[] cartItemDetails = new Object[]{1,"상품명"};
        when(cartRepository.findCartDetailsByUserId(1)).thenReturn(Collections.singletonList(cartItemDetails));
        when(CartItemMapper.INSTANCE.toDto(any())).thenReturn(new MypageCartItemsDto());

        List<MypageCartItemsDto> result = mypageService.getCartItemsForUser(1);

        assertFalse(result.isEmpty());
        verify(cartRepository).findCartDetailsByUserId(1);
    }

    @Test
    void getOrderHistoryForUser() throws Exception{
        String json = "[{\"id\":1,\"items\":[{\"itemId\":1,\"quantity\":2}]}]";
        when(orderRepository.findOrdersWithItemsByUserId(1)).thenReturn(json);
        when(objectMapper.readValue(json, new TypeReference<List<OrderHistoryDto>>() {}))
                .thenReturn(Collections.singletonList(new OrderHistoryDto()));

        List<OrderHistoryDto> result = mypageService.getOrderHistoryForUser(1);

        assertFalse(result.isEmpty());
        verify(orderRepository).findOrdersWithItemsByUserId(1);
    }
}