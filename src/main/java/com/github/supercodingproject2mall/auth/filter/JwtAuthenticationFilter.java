package com.github.supercodingproject2mall.auth.filter;

import com.github.supercodingproject2mall.auth.enums.ResponseType;
import com.github.supercodingproject2mall.auth.exception.ErrorType;
import com.github.supercodingproject2mall.auth.jwt.JwtTokenProvider;
import com.github.supercodingproject2mall.auth.response.ValidateTokenResponse;
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
        final String SIGNUP_URL =   "/api/auth/signup";
        final String LOGIN_URL =    "/api/auth/login";
        final String SWAGGER_URL =  "/swagger-ui/";
        final String API_DOCS_URL = "/api-docs/";
        final String ITEM_URL = "/api/item";
        String requestUri = request.getRequestURI().toString();
        String jwtToken = jwtTokenProvider.resolveToken(request);

        // TODO : 물품조회 uri 추가하기
        boolean isPublicUri = requestUri.equals(SIGNUP_URL) || requestUri.equals(LOGIN_URL) || requestUri.startsWith(ITEM_URL) ||
                requestUri.startsWith(SWAGGER_URL) || requestUri.startsWith(API_DOCS_URL);

        if (jwtToken == null) {
            if (isPublicUri) {
                filterChain.doFilter(request, response);
            }
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            return;
        }

        ValidateTokenResponse validateTokenResponse = jwtTokenProvider.validateToken(jwtToken);
        boolean isValidateToken = validateTokenResponse.getResponseType().toString().equals(ResponseType.SUCCESS.toString());
        boolean isExpiredToken = validateTokenResponse.getMessage().equals(ErrorType.EXPIRED_TOKEN.toString());

        if (isValidateToken) {
            Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }else {
            if (isExpiredToken) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
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
