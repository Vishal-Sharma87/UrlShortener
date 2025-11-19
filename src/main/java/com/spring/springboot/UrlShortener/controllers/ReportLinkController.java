package com.spring.springboot.UrlShortener.controllers;

import com.spring.springboot.UrlShortener.dto.ReportLinkRequestDto;
import com.spring.springboot.UrlShortener.dto.ReportLinkResponseDto;
import com.spring.springboot.UrlShortener.services.ReportLinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report-abuse")
@RequiredArgsConstructor
public class ReportLinkController {

    private final ReportLinkService reportLinkService;


    @PostMapping()
    public ResponseEntity<ReportLinkResponseDto> reportLink(@Valid @RequestBody ReportLinkRequestDto dto) {

        reportLinkService.tryAcceptingReport(dto);
        return null;
    }

}
