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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000",allowedHeaders = "*")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping(value = "/signup")
    public ResponseEntity<SignupResponse> signup(@Valid SignupDTO signupDTO, BindingResult bindingResult) {
        log.info(" =========== signup controller ===========");
        log.info("signupDTO = " + signupDTO);

        if(bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            SignupResponse signupResponse = new SignupResponse(errorMessage);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(signupResponse);
        }

        return authService.signup(signupDTO);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginDTO loginDTO, BindingResult bindingResult, HttpServletResponse response) {
        if(bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            LoginResponse loginResponse = new LoginResponse(errorMessage);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginResponse);
        }

        return authService.login(loginDTO, response);
    }

    @PostMapping(value = "/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;

        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refreshToken")) {
                refreshToken = cookie.getValue();
            }
        }

        Cookie cookie = authService.logout(refreshToken);

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    // TODO: 회원탈퇴
    @PatchMapping(value = "/delete_user")
    public void deleteUser(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveToken(request);

        authService.deleteUser(accessToken);
    }
}
