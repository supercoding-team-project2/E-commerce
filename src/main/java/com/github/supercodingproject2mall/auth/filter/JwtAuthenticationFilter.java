package com.github.supercodingproject2mall.auth.filter;

import com.github.supercodingproject2mall.auth.exception.ErrorType;
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
    private final String SIGNUP_URL = "/auth/signup";
    private final String LOGIN_URL = "/auth/login";
    private final String SWAGGER_URL = "/swagger-ui/";
    private final String API_DOCS_URL = "/api-docs/";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI().toString();
        String jwtToken = jwtTokenProvider.resolveToken(request);

        // TODO : 물품조회 uri 추가하기
        boolean isPublicUri = requestUri.equals(SIGNUP_URL) || requestUri.equals(LOGIN_URL) ||
                              requestUri.startsWith(SWAGGER_URL) || requestUri.startsWith(API_DOCS_URL);

        if (jwtToken == null) {
            if (isPublicUri) {
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
            if (jwtTokenProvider.validateToken(jwtToken).getMessage().equals(ErrorType.EXPIRED_TOKEN.toString())) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Token is expired");

                return;
            }
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");

            return;
        }

        filterChain.doFilter(request, response);
    }
}
