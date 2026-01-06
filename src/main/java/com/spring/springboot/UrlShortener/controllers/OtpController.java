package com.spring.springboot.UrlShortener.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.spring.springboot.UrlShortener.dto.requestDtos.OtpRequestDto;
import com.spring.springboot.UrlShortener.services.auth.OtpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verify")
@RequiredArgsConstructor
@Tag(
        name = "OTP",
        description = "One-time password generation and verification APIs"
)
public class OtpController {

    private final OtpService otpService;


    //    generate otp for a new user, reporter
    @Operation(
            summary = "Generate OTP for email verification",
            description = """
                    Generates a one-time password (OTP) and sends it to the provided email address.
                    
                    This OTP is used for:
                    - User signup verification
                    - Report-abuse verification
                    
                    OTP rules:
                    - OTP is email-specific
                    - OTP is time-bound
                    - OTP can be used only once
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "OTP generated and sent successfully",
            content = @Content(
                    mediaType = "text/plain",
                    examples = @ExampleObject(
                            value = "OTP sent successfully"
                    )
            )
    )
    @PostMapping("/generate-otp")
    public ResponseEntity<String> generateOtpForEmail(@Valid @RequestBody OtpRequestDto otpRequestDto) throws JsonProcessingException {

        otpService.sendOtp(otpRequestDto.getEntityEmail());

        String otpGenerationMessage = "OTP is sent to the email address: " + otpRequestDto.getEntityEmail();

        return ResponseEntity.ok(otpGenerationMessage);
    }


}
