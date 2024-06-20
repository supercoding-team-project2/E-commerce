package com.github.supercodingproject2mall.mypage.service;

import com.github.supercodingproject2mall.auth.repository.UserRepository;
import com.github.supercodingproject2mall.mypage.dto.MypageUserInfo;
import com.github.supercodingproject2mall.mypage.mapper.MypageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MypageService {
    private final UserRepository userRepository;

    public Optional<MypageUserInfo> findUserInfo(String userId){
        Integer userIdInt = Integer.valueOf(userId);
        return userRepository.findById(userIdInt).map(MypageMapper.INSTANCE::userEntityToMypage);
    }
}
