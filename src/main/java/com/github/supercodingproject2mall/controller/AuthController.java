package com.github.supercodingproject2mall.controller;

import com.github.supercodingproject2mall.auth.dto.LoginDTO;
import com.github.supercodingproject2mall.auth.dto.SignupDTO;
import com.github.supercodingproject2mall.auth.jwt.JwtTokenProvider;
import com.github.supercodingproject2mall.auth.response.LoginResponse;
import com.github.supercodingproject2mall.auth.response.SignupResponse;
import com.github.supercodingproject2mall.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping(value = "/signup")
    public SignupResponse signup(@Valid @RequestBody SignupDTO signupDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            throw new IllegalArgumentException(errorMessage);
        }

        return authService.signup(signupDTO);
    }

    @PostMapping(value = "/login")
    public LoginResponse login(@Valid @RequestBody LoginDTO loginDTO, BindingResult bindingResult, HttpServletResponse response) {
        if(bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            throw new IllegalArgumentException(errorMessage);
        }

        return authService.login(loginDTO, response);
    }

    @PostMapping(value = "/logout")
    public void logout(HttpServletRequest request) {
        String refreshToken = null;

        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refreshToken")) {
                refreshToken = cookie.getValue();
            }
        }

        log.info("refreshToken = " + refreshToken);

        authService.logout(refreshToken);
    }

    // TODO: 회원탈퇴
    @PatchMapping(value = "/delete_user")
    public void deleteUser(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveToken(request);

        authService.deleteUser(accessToken);
    }
}
