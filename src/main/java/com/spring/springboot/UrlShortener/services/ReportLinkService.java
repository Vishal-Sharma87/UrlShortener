package com.spring.springboot.UrlShortener.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.spring.springboot.UrlShortener.dto.ReportLinkRequestDto;
import com.spring.springboot.UrlShortener.entity.Links;
import com.spring.springboot.UrlShortener.exceptions.NoSuchLinkExists;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportLinkService {

    private final OtpService otpService;
    private final LinkService linkService;


    public String generateOtp(String email) throws JsonProcessingException {
        return otpService.sendOtp(email);
    }

    public void acceptReport(@Valid ReportLinkRequestDto dto) {
        /*
         * Steps to perform
         * step 1: -> validate otp
         * step 2: -> find Shortener url from database using link given by reporter
         * step 3: -> check if user have already reported this report or not
         * */

//        step 1: -> validate otp
        try {
            otpService.verifyOtp(dto.getReporterEmail(), dto.getOtp());
        } catch (Exception e) {

        }

//        if we are reaching this lines then the otp is verified and valid
//        step 2: -> find Shortener url from database using link given by reporter

//        extract hashedKey from the url
//        actualUrl = url.shortener/hashedKey
//        key = hashedKey
        String hashedKey = dto.getLinkToReport().substring(16);

        Links link = linkService.findLinkByHashedKey(hashedKey);

        if (link == null) {
            throw new NoSuchLinkExists("Invalid short url");
        }

//        if we are reaching this lines then the link is valid and needs to report
//        step 3: -> check if user have already reported this report or not

    }
}
