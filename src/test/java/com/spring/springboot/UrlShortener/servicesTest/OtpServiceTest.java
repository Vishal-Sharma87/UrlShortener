package com.spring.springboot.UrlShortener.servicesTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spring.springboot.UrlShortener.services.auth.OtpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OtpServiceTest {

    @Autowired
    private OtpService otpService;


    @Test
    void checkOtpSendingAndVerification() throws JsonProcessingException {

        String otp = "6330";

        boolean isValid = otpService.isValidOtp("vk@gmail.com", otp);

        int a = 5;


    }
}