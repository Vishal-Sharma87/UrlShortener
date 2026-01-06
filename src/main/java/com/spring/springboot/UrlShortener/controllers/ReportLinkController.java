package com.spring.springboot.UrlShortener.controllers;

import com.spring.springboot.UrlShortener.dto.requestDtos.ReportLinkRequestDto;
import com.spring.springboot.UrlShortener.services.ReportLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report-abuse")
@RequiredArgsConstructor
@Tag(
        name = "Report Abuse",
        description = "Community-driven abuse reporting APIs for short links"
)
public class ReportLinkController {

    private final ReportLinkService reportLinkService;



    @Operation(
            summary = "Report a suspicious or abusive short link",
            description = """
            Allows an end user to report a shortened URL that appears unsafe,
            misleading, or malicious.

            OTP verification is required to confirm reporter identity.
            Duplicate reports for the same link by the same reporter
            are not allowed until verification is completed.
            """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Report accepted successfully",
            content = @Content(
                    mediaType = "text/plain",
                    examples = @ExampleObject(
                            value = "We have accepted your request, we'll notify you once we have confirmation about the link"
                    )
            )
    )
    @PostMapping()
    public ResponseEntity<String> reportLink(@Valid @RequestBody ReportLinkRequestDto dto) {

        reportLinkService.tryAcceptingReport(dto);

        return new ResponseEntity<>("We have accepted your request, we'll notify you once we have confirmation about the link", HttpStatus.OK);
    }

}
