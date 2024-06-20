package com.github.supercodingproject2mall.auth.jwt;

import jakarta.servlet.http.HttpServletRequest;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import org.springframework.http.HttpHeaders;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String secretKey;
    private final long tokenValidMillisecond;
    private final UserDetailsService userDetailsService;

    public JwtTokenProvider(@Value("${spring.jwt.secret}") String secretKey,
                            @Value("${spring.jwt.token-validity-in-seconds}") long tokenValidMillisecond,
                            UserDetailsService userDetailsService)
    {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.tokenValidMillisecond = tokenValidMillisecond * 1000;
        this.userDetailsService = userDetailsService;
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    public boolean validateToken(String jwtToken) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwtToken).getBody();
            Date now = new Date();

            return claims.getExpiration().after(now);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            System.out.println("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            System.out.println("만료된 JWT 서명입니다.");
        } catch (UnsupportedJwtException e) {
            System.out.println("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            System.out.println("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, accessToken);
    }

    // TODO: refreshToken 생성
    public String createToken(String email, Integer id) {
        Claims claims = Jwts.claims()
                .setSubject(email);
        claims.put("userId", id);

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Integer getUserId(String accessToken) {
        return parseClaims(accessToken).get("userId", Integer.class);
    }
}
