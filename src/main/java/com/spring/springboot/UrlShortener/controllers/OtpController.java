package com.spring.springboot.UrlShortener.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.spring.springboot.UrlShortener.dto.OtpRequestDto;
import com.spring.springboot.UrlShortener.dto.OtpResponseDto;
import com.spring.springboot.UrlShortener.services.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/verify")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;


    //    generate otp for a reporter
    @PostMapping("/generate-otp")
    public ResponseEntity<OtpResponseDto> generateOtpForEmail(@Valid @RequestBody OtpRequestDto otpRequestDto) throws JsonProcessingException {

        String otpForEmail = otpService.sendOtp(otpRequestDto.getReporterEmail());

        String otpGenerationMessage = "OTP: " + otpForEmail + " generated for email: " + otpRequestDto.getReporterEmail();

        OtpResponseDto otpResponse = OtpResponseDto.builder()
                .otp(otpForEmail)
                .timestamp(LocalDateTime.now())
                .message(otpGenerationMessage)
                .build();
        return ResponseEntity.ok(otpResponse);
    }


}
