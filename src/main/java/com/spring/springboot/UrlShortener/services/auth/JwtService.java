package com.spring.springboot.UrlShortener.services.auth;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;


    public String generateJwt(String subject) {

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiry = new Date(nowMillis + 60 * 60 * 1000); // 10 minutes

        return Jwts.builder()
                .subject(subject)              // sub
                .claim("name", subject)           // custom claim
                .issuedAt(now)                 // iat
                .expiration(expiry)            // exp
                .signWith(
                        Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8))
                )
                .compact();
    }

    public String getSubjectFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
