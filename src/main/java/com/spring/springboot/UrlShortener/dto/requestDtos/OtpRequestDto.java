package com.spring.springboot.UrlShortener.dto.requestDtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        description = "Request payload used to generate an OTP for email verification"
)
public class OtpRequestDto {

    @Schema(
            description = "Email address to which the OTP will be sent",
            example = "user@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Email(message = "Enter a valid email id.")
    private String entityEmail;

    @Schema(
            description = "Name of the entity requesting OTP (user or reporter)",
            example = "Vishal",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    @NotBlank
    private String entityName;

}
