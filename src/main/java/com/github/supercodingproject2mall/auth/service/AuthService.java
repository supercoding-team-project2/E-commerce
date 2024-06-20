package com.github.supercodingproject2mall.auth.service;

import com.github.supercodingproject2mall.auth.dto.LoginDTO;
import com.github.supercodingproject2mall.auth.dto.SignupDTO;
import com.github.supercodingproject2mall.auth.dto.TokenDTO;
import com.github.supercodingproject2mall.auth.entity.enums.Gender;
import com.github.supercodingproject2mall.auth.entity.UserEntity;
import com.github.supercodingproject2mall.auth.jwt.JwtTokenProvider;
import com.github.supercodingproject2mall.auth.repository.UserRepository;
import com.github.supercodingproject2mall.auth.response.LoginResponse;
import com.github.supercodingproject2mall.auth.response.SignupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
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

    public LoginResponse login(LoginDTO loginDTO) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserEntity user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("이메일에 해당하는 유저가 없습니다: " + loginDTO.getEmail()));

        String accessToken = jwtTokenProvider.createToken(user.getEmail(), user.getId());

        TokenDTO tokenDTO = new TokenDTO(accessToken);

        return new LoginResponse("success", tokenDTO);
    }
}
