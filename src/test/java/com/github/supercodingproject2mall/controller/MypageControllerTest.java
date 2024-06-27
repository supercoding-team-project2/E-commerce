package com.github.supercodingproject2mall.controller;

import com.github.supercodingproject2mall.auth.jwt.JwtTokenProvider;
import com.github.supercodingproject2mall.mypage.dto.MypageCartItemsDto;
import com.github.supercodingproject2mall.mypage.dto.MypageUserInfo;
import com.github.supercodingproject2mall.mypage.service.MypageService;
import com.github.supercodingproject2mall.order.dto.OrderHistoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(MypageController.class)
class MypageControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MypageService mypageService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @Test
    @WithMockUser
    void findUserInfoTest() throws Exception{
        String token = jwtTokenProvider.createToken("access","jominsu@naver.com",1,600000L);
        when(jwtTokenProvider.resolveToken(any())).thenReturn(token);
        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getUserId(token)).thenReturn(1);
        when(mypageService.findUserInfo(1)).thenReturn(Optional.of(new MypageUserInfo()));

        mockMvc.perform(get("/api/mypage/user")
                        .header("Authorization","Bearer "+token))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getUserCartItemsTest() throws Exception{
        String token = jwtTokenProvider.createToken("access","jominsu@naver.com",1,600000L);
        when(jwtTokenProvider.resolveToken(any())).thenReturn(token);
        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getUserId(token)).thenReturn(1);
        when(mypageService.getCartItemsForUser(1)).thenReturn(Collections.singletonList(new MypageCartItemsDto()));

        mockMvc.perform(get("/api/mypage/cart"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getUserOrderTest() throws Exception{
        String token = jwtTokenProvider.createToken("access","jominsu@naver.com",1,600000L);
        when(jwtTokenProvider.resolveToken(any())).thenReturn(token);
        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getUserId(token)).thenReturn(1);
        when(mypageService.getOrderHistoryForUser(1)).thenReturn(Collections.singletonList(new OrderHistoryDto()));

        mockMvc.perform(get("/api/mypage/order"))
                .andExpect(status().isOk());
    }
}