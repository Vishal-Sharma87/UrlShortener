package com.spring.springboot.UrlShortener.dto;

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
public class OtpRequestDto {

    @Email(message = "Enter a valid email id.")
    private String reporterEmail;

    @NotNull(message = "Provide valid name")
    @NotBlank(message = "Name can't be empty")
    private String reporterName;

}
