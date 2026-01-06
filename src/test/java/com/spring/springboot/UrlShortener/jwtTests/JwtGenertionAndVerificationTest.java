package com.spring.springboot.UrlShortener.jwtTests;

import com.spring.springboot.UrlShortener.services.auth.JwtService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtGenertionAndVerificationTest {


    @Autowired
    private JwtService jwtService;

    @Test
    @Disabled
    void test() {
        String jwt = jwtService.generateJwt("Vishal");

        jwtService.getSubjectFromJwtToken(jwt);

        jwtService.getSubjectFromJwtToken(jwt.toLowerCase());


    }

}
