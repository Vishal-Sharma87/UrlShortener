package com.spring.springboot.UrlShortener.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spring.springboot.UrlShortener.dto.OtpRequestDto;
import com.spring.springboot.UrlShortener.dto.OtpResponseDto;
import com.spring.springboot.UrlShortener.dto.ReportLinkRequestDto;
import com.spring.springboot.UrlShortener.dto.ReportLinkResponseDto;
import com.spring.springboot.UrlShortener.services.OtpService;
import com.spring.springboot.UrlShortener.services.ReportLinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/report-abuse")
@RequiredArgsConstructor
public class ReportLinkController {

    private final ReportLinkService reportLinkService;


    @PostMapping()
    public ResponseEntity<ReportLinkResponseDto> reportLink(@Valid @RequestBody ReportLinkRequestDto dto) {

        reportLinkService.acceptReport(dto);
        return null;
    }

}
