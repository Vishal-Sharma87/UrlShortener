package com.spring.springboot.UrlShortener.dto.RedisDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpRedisDocDto { //used to save the hashed otp and metadata in Redis
    private String hashedOtp;          // hashed OTP
    private Long createdAt;
    private Long expiresAt;
    private boolean used;
}
