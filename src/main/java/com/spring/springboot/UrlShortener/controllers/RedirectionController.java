package com.spring.springboot.UrlShortener.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spring.springboot.UrlShortener.serviceDtos.serviceResponseDtos.RedirectServiceResponseDto;
import com.spring.springboot.UrlShortener.services.RedirectService;
import com.spring.springboot.UrlShortener.utils.virusTotalUtils.virusTotalServices.FinalVerdict;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

//@RestController
@Controller // using @Controller because it supports html page rendering and user clicks
@RequestMapping("/url.shortener")
@RequiredArgsConstructor
public class RedirectionController {

    private final RedirectService redirectService;


    @GetMapping("/{hash}")
    public String redirectionFromHashToLongUrl(@PathVariable String hash, Model model) throws JsonProcessingException {

        RedirectServiceResponseDto url = redirectService.getActualUrlIfExists(hash);

        /*
         * the link is found now there are three main options associated with status of link
         *
         * case 1 "SAFE" -> analysis + normal flow of redirection
         *
         * case 2 "SUSPICIOUS" -> a page to confirm "the link is reported suspicious continue redirection?"
         *                           if yes -> redirection
         *                           else user will take care
         *
         * case 3 "MALICIOUS" -> no redirection in any scenario
         */

        switch (url.getStatus()) {
            case FinalVerdict.Verdict.SAFE:
                // Direct redirect
                return "redirect:" + url.getActualUrl();

            case FinalVerdict.Verdict.SUSPICIOUS, FinalVerdict.Verdict.PENDING_REVERIFICATION,
                 FinalVerdict.Verdict.UNVERIFIED:
                // Show warning + confirm
                model.addAttribute("shortCode", hash);
                model.addAttribute("longUrl", url.getActualUrl());
                return "suspicious-warning";

            case FinalVerdict.Verdict.MALICIOUS:
                // Show blocked page
                model.addAttribute("longUrl", url.getActualUrl());
                return "malicious-warning";

            default:
                model.addAttribute("message", "Unknown URL status");
                return "error";
        }

    }


    @GetMapping("/confirm/{shortCode}")
    public ResponseEntity<?> confirmAndRedirect(@PathVariable String shortCode) throws JsonProcessingException {

        RedirectServiceResponseDto res = redirectService.getActualUrlIfExists(shortCode);

        if (res == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Invalid or expired short link");
        }

        // Redirect to the original long URL
        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, res.getActualUrl())
                .build();
    }

}
