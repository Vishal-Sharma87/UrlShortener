package com.spring.springboot.UrlShortener.dto.requestDtos;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
@Schema(
        description = "Request payload used to register a new user with OTP verification"
)
public class SignupRequestDto {

    @Schema(
            description = "Unique username for the user",
            example = "vishal123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String userName;

    @Schema(
            description = "Email address used for OTP verification",
            example = "user@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Email
    private String email;

    @Schema(
            description = "User password",
            example = "StrongPassword@123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String password;

    @Schema(
            description = """
                    One-time password sent to the user's email.
                    
                    OTP must be:
                    - generated for the same email
                    - unused
                    - correct
                    - not expired
                    """,
            example = "483921",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String otp;
}
