package com.spring.springboot.UrlShortener.services.jwtAuth;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class JwtService {


    private String secretKey = "U7j39o1yVJ3a83kd1s+pd9P1C2QWmJH93sahEKS9oFUGYp4YlNQW3kdz0O8tE9nK";


    public String generateJwt(String subject) {

        HashMap<String, Object> defaultClaims = new HashMap<>();
        defaultClaims.put("name", subject);
        defaultClaims.put("iat", new Date());
        defaultClaims.put("exp", System.currentTimeMillis() + 300000L);

        return Jwts.builder()
                .subject(subject)
                .claims(defaultClaims)
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
