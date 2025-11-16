package com.spring.springboot.UrlShortener.dto;


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
public class ReportLinkRequestDto {


    @NotBlank
    @NotNull
    private String reporterName;

    @Email
    private String reporterEmail;

    @NotNull
    @NotBlank
    private String linkToReport;

    @NotNull
    @NotBlank
    private List<String> cause;

    private String description;

    @NotNull
    @NotBlank
    private String otp;
}
