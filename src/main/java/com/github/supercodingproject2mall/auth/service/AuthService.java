package com.github.supercodingproject2mall.auth.service;

import com.github.supercodingproject2mall.auth.dto.LoginDTO;
import com.github.supercodingproject2mall.auth.dto.SignupDTO;
import com.github.supercodingproject2mall.auth.dto.TokenDTO;
import com.github.supercodingproject2mall.auth.entity.RefreshEntity;
import com.github.supercodingproject2mall.auth.enums.Gender;
import com.github.supercodingproject2mall.auth.entity.UserEntity;
import com.github.supercodingproject2mall.auth.enums.UserStatus;
import com.github.supercodingproject2mall.auth.exception.ErrorType;
import com.github.supercodingproject2mall.auth.jwt.JwtTokenProvider;
import com.github.supercodingproject2mall.auth.repository.RefreshRepository;
import com.github.supercodingproject2mall.auth.repository.UserRepository;
import com.github.supercodingproject2mall.auth.response.LoginResponse;
import com.github.supercodingproject2mall.auth.response.SignupResponse;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<SignupResponse> signup(SignupDTO signupDTO) {

        if(userRepository.findByEmail(signupDTO.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SignupResponse(ErrorType.DUPLICATE_USER.getMessage(), signupDTO.getEmail()));
        }

        UserEntity user = userRepository.findByEmail(signupDTO.getEmail())
                                        .orElseGet(() -> userRepository.save(UserEntity.builder()
                                                                       .email(signupDTO.getEmail())
                                                                       .password(passwordEncoder.encode(signupDTO.getPassword()))
                                                                       .address(signupDTO.getAddress())
                                                                       .phoneNum(signupDTO.getPhoneNum())
                                                                       .gender(Gender.valueOf(signupDTO.getGender())).build()));

        return ResponseEntity.status(HttpStatus.CREATED).body(new SignupResponse("success", user.getEmail()));
    }

    public ResponseEntity<LoginResponse> login(LoginDTO loginDTO, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserEntity user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("이메일에 해당하는 유저가 없습니다: " + loginDTO.getEmail()));

        String accessToken = jwtTokenProvider.createToken("access", user.getEmail(), user.getId(), 600000L); // 10분
//        String accessToken = jwtTokenProvider.createToken("access", user.getEmail(), user.getId(), 600000L); // 10분
        String refreshToken = jwtTokenProvider.createToken("refresh", user.getEmail(), user.getId(), 86400000L);

        addRefreshEntity(user.getEmail(), refreshToken, 86400000L);
        
        response.addCookie(createCookie("refresh", refreshToken));
        response.setStatus(HttpStatus.OK.value());

        TokenDTO tokenDTO = new TokenDTO(accessToken, refreshToken);

        return ResponseEntity.ok(new LoginResponse("success", tokenDTO));
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

    public Cookie logout(String refreshToken) {
        if(refreshToken == null) {
            throw new JwtException(ErrorType.NULL_TOKEN.getMessage());
        }

        Boolean isExist = refreshRepository.existsByRefresh(refreshToken);
        if(!isExist) {
            throw new JwtException("리프레시토큰을 찾을 수 없습니다.");
        }

        refreshRepository.deleteByRefresh(refreshToken);

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        return cookie;
    }

    public void deleteUser(String accessToken) {
        String email = jwtTokenProvider.parseClaims(accessToken).getSubject();

        UserEntity foundUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("이메일에 해당하는 유저가 없습니다."));

        userRepository.save(foundUser.toBuilder().status(UserStatus.DELETED).build());
    }
}
