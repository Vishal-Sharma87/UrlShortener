package com.spring.springboot.UrlShortener.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.spring.springboot.UrlShortener.dto.RedirectServiceResponseDto;
import com.spring.springboot.UrlShortener.dto.TrackPayloadDto;
import com.spring.springboot.UrlShortener.services.AnalysisService;
import com.spring.springboot.UrlShortener.services.RedirectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(
        name = "Analytics",
        description = "APIs responsible for capturing click analytics during redirection"
)
public class IntermediateRedirectingController {

    private final RedirectService redirectService;
    private final AnalysisService analysisService;

    @Operation(
            summary = "Capture click analytics for a short URL",
            description = """
                    This endpoint is called automatically from an intermediate tracking page
                    during short URL redirection.
                    
                    It captures device and browser metadata and forwards it asynchronously
                    for analysis. The request is handled in a fire-and-forget manner and
                    does not block user redirection.
                    """
    )
    @ApiResponse(
            responseCode = "204",
            description = "Analytics data accepted successfully",
            content = @Content
    )
    @PostMapping("/track")
    public void trackUserInformationForLinkAnalysis(@RequestBody TrackPayloadDto dto, HttpServletRequest request, HttpServletResponse response, Model model) {

        analysisService.sendDataToKafka(dto, request);

    }

    @Operation(
            summary = "Confirm suspicious link and continue redirection",
            description = """
                    Used after user confirmation for suspicious or unverified links.
                    Once confirmed, the user is redirected via the analytics tracking page.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Confirmation successful, redirection initiated"
    )
    @GetMapping("/confirm/{shortCode}")
    public String confirmAndRedirect(@PathVariable String shortCode, Model model) throws JsonProcessingException {

        RedirectServiceResponseDto res = redirectService.getActualUrlIfExists(shortCode);

        if (res == null) {
            return "error";
        }

        // Redirect to the original long URL
        model.addAttribute("shortCode", shortCode);
        model.addAttribute("longUrl", res.getLongUrl());

//        serve an intermediate html page to get user device info
        return "track";
    }

}
