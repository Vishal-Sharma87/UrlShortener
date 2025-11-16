package com.spring.springboot.UrlShortener.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Username cannot be blank")
    private String userName;

    @NotBlank(message = "Password cannot be blank")
//    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
}