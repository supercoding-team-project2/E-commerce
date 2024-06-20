package com.github.supercodingproject2mall.controller;

import com.github.supercodingproject2mall.auth.dto.LoginDTO;
import com.github.supercodingproject2mall.auth.dto.SignupDTO;
import com.github.supercodingproject2mall.auth.response.LoginResponse;
import com.github.supercodingproject2mall.auth.response.SignupResponse;
import com.github.supercodingproject2mall.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/signup")
    public SignupResponse signup(@Valid @RequestBody SignupDTO signupDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            throw new IllegalArgumentException(errorMessage);
        }

        return authService.signup(signupDTO);
    }

    @PostMapping(value = "/login")
    public LoginResponse login(@Valid @RequestBody LoginDTO loginDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            throw new IllegalArgumentException(errorMessage);
        }

        return authService.login(loginDTO);
    }
}
