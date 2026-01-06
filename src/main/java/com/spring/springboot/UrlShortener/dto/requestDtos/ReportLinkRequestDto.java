package com.spring.springboot.UrlShortener.dto.requestDtos;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        description = "Request payload used to report an abusive or suspicious short link"
)
public class ReportLinkRequestDto {

    @Schema(
            description = "Name of the person reporting the link",
            example = "John Doe",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    @NotNull
    private String reporterName;

    @Schema(
            description = "Email address of the reporter (used for OTP verification)",
            example = "john@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Email
    private String reporterEmail;

    @Schema(
            description = "Short URL that is being reported",
            example = "https://sho.rt/aZ91Qe",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    @NotNull
    private String linkToReport;

    @Schema(
            description = "One or more reasons for reporting the link",
            example = "[\"Phishing\", \"Scam\"]",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    private List<String> cause;

    @Schema(
            description = "Optional additional description explaining the abuse",
            example = "The link redirects to a fake banking login page"
    )
    private String description;

    @Schema(
            description = """
                    One-time password used to verify the reporter's email.
                    
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
    @NotNull
    private String otp;
}
