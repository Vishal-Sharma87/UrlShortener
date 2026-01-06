package com.spring.springboot.UrlShortener.controllers;


import com.spring.springboot.UrlShortener.advices.exceptions.ResourceNotExistsException;
import com.spring.springboot.UrlShortener.dto.UrlToShortRequestDto;
import com.spring.springboot.UrlShortener.dto.responseDtos.LinkAsResponseDto;
import com.spring.springboot.UrlShortener.dto.responseDtos.LinkCreationResponseDto;
import com.spring.springboot.UrlShortener.dto.responseDtos.LinkQueryResponseDto;
import com.spring.springboot.UrlShortener.entity.Links;
import com.spring.springboot.UrlShortener.services.links.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/link")
@RequiredArgsConstructor
@Tag(
        name = "URL Management",
        description = "APIs to create, fetch, and delete short URLs for authenticated users"
)
public class LinkController {

    private final LinkService linkService;


    //    Create
    @Operation(
            summary = "Create a short URL",
            description = """
                    Creates a shortened URL for the provided long URL.
                    
                    Rules:
                    - User must be authenticated
                    - Short URL is owned by the authenticated user
                    - Safety analysis is performed asynchronously
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Short URL created successfully",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = """
                                    {
                                      "message": "Short link created successfully",
                                      "status": "SUCCESS",
                                      "shortUrl": "https://url.shortener/aZ91Qe"
                                    }
                                    """
                    )
            )
    )
    @PostMapping("/create")
    public ResponseEntity<LinkCreationResponseDto> createNewShortLink(@RequestBody UrlToShortRequestDto urlDto) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

//      a  method to create a working url from a urlDto
        String shortUrl = linkService.generateShortUrl(urlDto, userName);

//        creating a response to return
        LinkCreationResponseDto response = LinkCreationResponseDto
                .builder()
                .shortUrl(shortUrl)
                .message("Short URL created and queued for processing.")
                .status("processing")
                .build();

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    //    read all
    @Operation(
            summary = "Get all short links of the logged-in user",
            description = """
                    Retrieves all short URLs created by the authenticated user.
                    
                    Optional filters can be applied using query parameters.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Links fetched successfully"
    )
    @GetMapping("/get-all-links")
    public ResponseEntity<LinkQueryResponseDto<List<LinkAsResponseDto>>> getAllLinksOfAnUser(@RequestParam(defaultValue = "all") String type) {
        try {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();

//        calling getAllLinksOfAnUser method of linkService which expect type of links to
//        be provided from path parameter,
//        default is all -> safe + sus + mal,
//        safe -> safe,
//        unsafe -> malicious + suspicious
//        else -> invalid query
            List<LinkAsResponseDto> allLinksOfAnUser = linkService.getAllLinksOfAnUser(userName, type);

            return ResponseEntity.ok(new LinkQueryResponseDto<>(allLinksOfAnUser, "Content found", new Date()));

        } catch (ResourceNotExistsException e) {
            return new ResponseEntity<>(new LinkQueryResponseDto<>(null, e.getMessage(), new Date()), HttpStatus.NOT_FOUND);
        }
    }

    //    read one
    @Operation(
            summary = "Get a single short link",
            description = """
                    Fetches a short link owned by the authenticated user.
                    
                    Query rules:
                    - 'by' can be 'id' or 'hash'
                    - 'value' must match the selected type
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Link fetched successfully",
            content = @Content(
                    mediaType = "application/json"
            )
    )
    @GetMapping("/get-link")
    public ResponseEntity<LinkAsResponseDto> getLinkByIdOrHashedKey(@RequestParam() String by, @RequestParam() String value) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        LinkAsResponseDto linkResponse = linkService.findLink(userName, by, value);
        return new ResponseEntity<>(linkResponse, HttpStatus.OK);
    }

    //    delete by id/hash
    @Operation(
            summary = "Delete a short link",
            description = """
                    Deletes a short URL owned by the authenticated user.
                    
                    Query rules:
                    - 'by' can be 'id' or 'hash'
                    - 'value' must match the selected type
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Link deleted successfully"
    )
    @DeleteMapping("/delete-link")
    public ResponseEntity<Links> deleteLinkByIdOrHashedKey(@RequestParam() String by, @RequestParam() String value) {


        linkService.deleteLink(by, value);
        return new ResponseEntity<>(HttpStatus.OK);
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //    delete all
    @Operation(
            summary = "Delete all short links of the logged-in user",
            description = """
                    Deletes all short URLs owned by the authenticated user.
                    
                    Optional type filter can be applied.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "All links deleted successfully"
    )
    @DeleteMapping("/delete-all-links")
    public ResponseEntity<String> deleteAllLinksOfAnUser(@RequestParam(defaultValue = "all") String type) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        linkService.deleteAllLinksOfAnUserByType(userName, type);

        return new ResponseEntity<>(type + " links are deleted from database", HttpStatus.OK);
    }

//there should be no update mapping because updating an existing url will result in new url creation


}
