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


    @NotBlank(message = "Name is mandatory")
    @NotNull(message = "Name is mandatory")
    private String reporterName;

    @Email(message = "Email is required.")
    private String reporterEmail;

    @NotNull(message = "Provide link to report is mandatory")
    @NotBlank(message = "Provide link to report is mandatory")
    private String linkToReport;

    @NotNull(message = "Give at-least one reason of abuse report.")
    @NotBlank(message = "Give at-least one reason of abuse report.")
    private List<String> cause;

    private String description;

    @NotNull(message = "Otp is required")
    @NotBlank(message = "Otp can't be empty")
    private String otp;
}
