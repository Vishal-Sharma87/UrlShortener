package com.spring.springboot.UrlShortener.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(
        description = "Request payload used to generate a shortened URL from a long URL"
)
public class UrlToShortRequestDto {

    @Schema(
            description = "The original long URL that needs to be shortened",
            example = "https://example.com/very/long/url?with=params",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "URL cannot be null")
    @NotBlank(message = "URL cannot be blank")
    private String actualUrl;
}
