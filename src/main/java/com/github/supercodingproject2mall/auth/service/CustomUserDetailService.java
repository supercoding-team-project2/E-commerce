package com.github.supercodingproject2mall.auth.service;

import com.github.supercodingproject2mall.auth.dto.CustomUserDetails;
import com.github.supercodingproject2mall.auth.entity.UserEntity;
import com.github.supercodingproject2mall.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("이메일에 해당하는 유저가 없습니다."));

        CustomUserDetails customUserDetails = CustomUserDetails.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        return customUserDetails;
    }
}
