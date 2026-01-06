package com.spring.springboot.UrlShortener.dto.requestDtos;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(
        description = "Request payload used to authenticate a user"
)
public class LoginRequestDto {

    @Schema(
            description = "Registered username",
            example = "vishal123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String userName;

    @Schema(
            description = "User password",
            example = "StrongPassword@123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String password;
}