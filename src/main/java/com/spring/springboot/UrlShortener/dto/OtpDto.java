package com.spring.springboot.UrlShortener.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpDto {
    private String hashedOtp;          // hashed OTP
    private Long createdAt;
    private Long expiresAt;
    private boolean used;
}
