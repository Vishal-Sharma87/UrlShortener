package com.spring.springboot.UrlShortener.controllers;

import com.spring.springboot.UrlShortener.dto.requestDtos.LoginRequestDto;
import com.spring.springboot.UrlShortener.dto.requestDtos.SignupRequestDto;
import com.spring.springboot.UrlShortener.dto.responseDtos.AuthResponseDto;
import com.spring.springboot.UrlShortener.services.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(
        name = "Authentication",
        description = "User authentication and authorization APIs (OTP + JWT)"
)
public class AuthController {

    private final AuthService authService;

    // Signup endpoint
    @Operation(
            summary = "User signup with OTP verification",
            description = """
            Registers a new user after validating an OTP sent to the user's email.

            Signup rules:
            - OTP must be generated for the same email
            - OTP must be unused, correct, and not expired
            - Username and email must be unique
            """
    )
    @ApiResponse(
            responseCode = "201",
            description = "User registered successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthResponseDto.class),
                    examples = @ExampleObject(
                            value = """
                {
                  "data": "<JWT_TOKEN>",
                  "message": "User registered successfully",
                  "timestamp": "2026-01-06T12:35:00"
                }
                """
                    )
            )
    )
    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto<String>> signup(@Valid @RequestBody SignupRequestDto request) {

        authService.registerUser(request);
//        user create ask him to login

        AuthResponseDto<String> successfulRegistration = new AuthResponseDto<>("User registered successfully", "SUCCESS", LocalDateTime.now());

        return ResponseEntity.ok(successfulRegistration);
    }

    // Login endpoint
    @Operation(
            summary = "User login",
            description = """
            Authenticates an existing user using username and password.

            On successful authentication, a JWT token is returned.
            The token must be used as a Bearer token for protected APIs.
            """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Login successful",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthResponseDto.class),
                    examples = @ExampleObject(
                            value = """
                {
                  "data": "<JWT_TOKEN>",
                  "message": "Login successful",
                  "timestamp": "2026-01-06T12:40:00"
                }
                """
                    )
            )
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto<String>> login(@Valid @RequestBody LoginRequestDto request) {
//        check credentials and if correct generate JWT using username

        String generatedJwtForLogin = authService.loginUser(request);

        AuthResponseDto<String> successfulLoginResponse = new AuthResponseDto<>(generatedJwtForLogin, "Login successful", LocalDateTime.now());

        return ResponseEntity.ok(successfulLoginResponse);
    }
}