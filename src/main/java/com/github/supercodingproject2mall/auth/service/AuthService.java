package com.github.supercodingproject2mall.auth.service;

import com.github.supercodingproject2mall.auth.dto.LoginDTO;
import com.github.supercodingproject2mall.auth.dto.SignupDTO;
import com.github.supercodingproject2mall.auth.dto.TokenDTO;
import com.github.supercodingproject2mall.auth.entity.RefreshEntity;
import com.github.supercodingproject2mall.auth.enums.Gender;
import com.github.supercodingproject2mall.auth.entity.UserEntity;
import com.github.supercodingproject2mall.auth.jwt.JwtTokenProvider;
import com.github.supercodingproject2mall.auth.repository.RefreshRepository;
import com.github.supercodingproject2mall.auth.repository.UserRepository;
import com.github.supercodingproject2mall.auth.response.LoginResponse;
import com.github.supercodingproject2mall.auth.response.SignupResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshRepository refreshRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public SignupResponse signup(SignupDTO signupDTO) {

        UserEntity user = userRepository.findByEmail(signupDTO.getEmail())
                                        .orElseGet(() -> userRepository.save(UserEntity.builder()
                                                                       .email(signupDTO.getEmail())
                                                                       .password(passwordEncoder.encode(signupDTO.getPassword()))
                                                                       .address(signupDTO.getAddress())
                                                                       .phoneNum(signupDTO.getPhoneNum())
                                                                       .gender(Gender.valueOf(signupDTO.getGender())).build()));

        return new SignupResponse("success", user.getEmail());
    }

    public LoginResponse login(LoginDTO loginDTO, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserEntity user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("이메일에 해당하는 유저가 없습니다: " + loginDTO.getEmail()));

        String accessToken = jwtTokenProvider.createToken("access", user.getEmail(), user.getId(), 60000L);
        String refreshToken = jwtTokenProvider.createToken("refresh", user.getEmail(), user.getId(), 86400000L);

        addRefreshEntity(user.getEmail(), refreshToken, 86400000L);
        
        response.addCookie(createCookie("refresh", refreshToken));
        response.setStatus(HttpStatus.OK.value());

        TokenDTO tokenDTO = new TokenDTO(accessToken, refreshToken);

        return new LoginResponse("success", tokenDTO);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(true);

        return cookie;
    }

    private void addRefreshEntity(String email, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);

        refreshRepository.save(RefreshEntity.builder()
                .email(email)
                .refresh(refresh)
                .expiration(date.toString())
                .build());
    }
}
