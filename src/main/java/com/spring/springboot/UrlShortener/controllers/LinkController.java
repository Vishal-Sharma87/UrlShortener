package com.spring.springboot.UrlShortener.controllers;


import com.spring.springboot.UrlShortener.dto.UrlToShort;
import com.spring.springboot.UrlShortener.entity.Links;
import com.spring.springboot.UrlShortener.exceptions.ResourceNotExistsException;
import com.spring.springboot.UrlShortener.responseDtos.LinkApiResponseDto;
import com.spring.springboot.UrlShortener.responseDtos.LinkCreationResponseDto;
import com.spring.springboot.UrlShortener.services.LinkService;
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
public class LinkController {

    private final LinkService linkService;


    //    CRUD
    @PostMapping("/create")
    public ResponseEntity<LinkCreationResponseDto> createNewShortLink(@RequestBody UrlToShort url) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

//        calling the actualUrlToShortenHashConversion method of linkService to push the required info into kafka and then return a hashedKey
        String hashedKey = linkService.actualUrlToShortHashConversion(url.getActualUrl(), userName);

// a separate method to create a working url from a generatedHashed
        String shortUrl = linkService.generateShortUrl(hashedKey);


//        creating a response to return
        LinkCreationResponseDto response = LinkCreationResponseDto.builder().shortUrl(shortUrl).message("Short URL created and queued for processing.").status("processing").build();

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    //    READ
    @GetMapping("/get-all-links")
    public ResponseEntity<LinkApiResponseDto<List<Links>>> getAllLinksOfAnUser(@RequestParam(defaultValue = "all") String type) {
        try {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();

//        calling getAllLinksOfAnUser method of linkService which expect type of links to
//        be provided from path parameter,
//        default is all -> safe + sus + mal,
//        safe -> safe,
//        unsafe -> malicious + suspicious
//        else -> invalid query
            List<Links> allLinksOfAnUser = linkService.getAllLinksOfAnUser(userName, type);
            return ResponseEntity.ok(new LinkApiResponseDto<>(allLinksOfAnUser, "Content found", new Date()));

        } catch (ResourceNotExistsException e) {
            return new ResponseEntity<>(new LinkApiResponseDto<>(null, e.getMessage(), new Date()), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/get-link")
    public ResponseEntity<Links> getLinkByIdOrHashedKey(@RequestParam() String by, @RequestParam() String value) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();


        Links link = linkService.findLink(userName, by, value);

        if (link != null) {
            return new ResponseEntity<>(link, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete-link")
    public ResponseEntity<Links> deleteLinkByIdOrHashedKey(@RequestParam() String by, @RequestParam() String value) {


        linkService.deleteLink(by, value);
        return new ResponseEntity<>(HttpStatus.OK);
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//there should be no update mapping because updating an existing url will result in new url creation

    @DeleteMapping("/delete-all-links")
    public ResponseEntity<String> deleteAllLinksOfAnUser(@RequestParam(defaultValue = "all") String type) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

//        TODO will find a way to handle if user have no links to delete then what to return
        linkService.deleteAllLinksOfAnUserByType(userName, type);

        return new ResponseEntity<>(type + " links are deleted from database", HttpStatus.OK);
//        return new ResponseEntity<>("There is no links to delete", HttpStatus.NOT_FOUND);
    }

}
