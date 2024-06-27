package com.github.supercodingproject2mall.auth.filter;

import com.github.supercodingproject2mall.auth.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
 * <<< Filter >>>>
 * 1. 요청을 처리하거나 요청을 가로채어 응답을 수정하는 역할.
 * 2. 필터체인의 일부로서 요청이 체인을 통해 계속 전달되도록 할 수 있음.
 * 3. 응답을 직접 작성하여 요청 처리를 종료할 수도 있음.
 * */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = jwtTokenProvider.resolveToken(request);
        log.info("jwtToken = " + jwtToken);

        if (jwtToken == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("token is null");

            return;
        }

        if (jwtTokenProvider.validateToken(jwtToken).getResponseType().toString().equals("SUCCESS")) {
            Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        if (!jwtTokenProvider.validateToken(jwtToken).getResponseType().toString().equals("SUCCESS")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");

            return;
        }

        filterChain.doFilter(request, response);
    }
}
