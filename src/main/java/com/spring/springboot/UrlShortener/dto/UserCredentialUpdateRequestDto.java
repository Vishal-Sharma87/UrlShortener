package com.spring.springboot.UrlShortener.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
@Schema(
        description = "Request payload used to update credentials of the logged-in user"
)
public class UserCredentialUpdateRequestDto {

    @Schema(
            description = """
            Username to update.

            NOTE:
            This field is optional and ignored if provided.
            The actual username is resolved from the authenticated JWT.
            """,
            example = "vishal123"
    )
    private String userName;

    @Schema(
            description = "New password for the user account",
            example = "NewStrongPassword@123"
    )
    private String password;

    @Schema(
            description = "New email address for the user account",
            example = "new-email@example.com"
    )
    private String email;
}
