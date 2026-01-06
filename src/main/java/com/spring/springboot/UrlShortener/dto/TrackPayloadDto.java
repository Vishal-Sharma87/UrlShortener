package com.spring.springboot.UrlShortener.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Schema(
        description = "Payload sent from client browser to capture click analytics"
)
public class TrackPayloadDto {
    // used to get clicker's device's, browser's, etc. info for real time analysis


    @Schema(
            description = "Short hash identifying the clicked URL",
            example = "aZ91Qe",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String shortHash;

    @Schema(
            description = "Device screen width used to classify device type",
            example = "1920"
    )
    private Integer screenWidth;

    @Schema(
            description = "Browser viewport width",
            example = "1200"
    )
    private Integer viewportWidth;

    @Schema(
            description = "Browser user-agent string",
            example = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
    )
    private String userAgent;

    @Schema(
            description = "User's local timezone",
            example = "Asia/Kolkata"
    )
    private String timezone;
}

