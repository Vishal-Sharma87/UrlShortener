package com.spring.springboot.UrlShortener.services.jwtAuth;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;


    public String generateJwt(String subject) {

        HashMap<String, Object> defaultClaims = new HashMap<>();
        defaultClaims.put("name", subject);
        defaultClaims.put("iat", new Date());
        defaultClaims.put("exp", System.currentTimeMillis() + 300000L);

        return Jwts.builder()
                .subject(subject)
                .claims(defaultClaims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 10 * 10 ))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
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
