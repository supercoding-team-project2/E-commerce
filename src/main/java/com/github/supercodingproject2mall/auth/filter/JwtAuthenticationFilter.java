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

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI().toString();
        String jwtToken = jwtTokenProvider.resolveToken(request);

        // TODO : 물품조회 uri 추가하기
        if (jwtToken == null) {
            if(requestUri.equals("/api/auth/signup") || requestUri.equals("/api/auth/login")) {
                filterChain.doFilter(request, response);
            }
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

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
