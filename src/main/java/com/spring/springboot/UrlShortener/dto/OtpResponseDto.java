package com.spring.springboot.UrlShortener.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpResponseDto {

    private String otp;
    private String message;
    private LocalDateTime timestamp;
}
