package com.github.supercodingproject2mall.auth.jwt;

import com.github.supercodingproject2mall.auth.enums.ResponseType;
import com.github.supercodingproject2mall.auth.exception.ErrorType;
import com.github.supercodingproject2mall.auth.response.ValidateTokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import org.springframework.http.HttpHeaders;

import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {
    private final String secretKey;
    private final UserDetailsService userDetailsService;

    public JwtTokenProvider(@Value("${spring.jwt.secret}") String secretKey, UserDetailsService userDetailsService) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.userDetailsService = userDetailsService;
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return bearerToken;
    }

    public ValidateTokenResponse validateToken(String accessToken) {
        Boolean isNotExpired = isNotExpired(accessToken);
        Claims claims = null;

        if (isNotExpired) {
            try {
                claims = parseClaims(accessToken);
            } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
                return new ValidateTokenResponse(ResponseType.ERROR, ErrorType.INVALID_TOKEN_SIGNATURE.getMessage());
            } catch (UnsupportedJwtException e) {
                return new ValidateTokenResponse(ResponseType.ERROR, ErrorType.UNSUPPORTED_TOKEN.getMessage());
            } catch (IllegalArgumentException e) {
                return new ValidateTokenResponse(ResponseType.ERROR, ErrorType.INVALID_TOKEN.getMessage());
            }

            return new ValidateTokenResponse(ResponseType.SUCCESS, claims);
        }

        return new ValidateTokenResponse(ResponseType.ERROR, ErrorType.EXPIRED_TOKEN.getMessage());
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, accessToken);
    }

    public String createToken(String tokenType, String email, Integer id, Long expiredMs) {
        Claims claims = Jwts.claims()
                .setSubject(email);
        claims.put("tokenType", tokenType);
        claims.put("userId", id);

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiredMs))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Integer getUserId(String jwtToken) {
        return parseClaims(jwtToken).get("userId", Integer.class);
    }

    public String getEmail(String jwtToken) {
        return parseClaims(jwtToken).getSubject();
    }

    public String getCategory(String jwtToken) {
        return parseClaims(jwtToken).get("category", String.class);
    }

    public Boolean isNotExpired(String jwtToken) {
        return parseClaims(jwtToken).getExpiration().after(new Date());
    }
}
