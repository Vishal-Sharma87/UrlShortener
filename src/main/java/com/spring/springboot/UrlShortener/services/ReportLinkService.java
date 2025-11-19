package com.spring.springboot.UrlShortener.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.spring.springboot.UrlShortener.dto.ReportLinkRequestDto;
import com.spring.springboot.UrlShortener.entity.AbuseReport;
import com.spring.springboot.UrlShortener.entity.Links;
import com.spring.springboot.UrlShortener.exceptions.InvalidOTPException;
import com.spring.springboot.UrlShortener.exceptions.LinkAlreadyReportedByCurrentEmailOfReporterException;
import com.spring.springboot.UrlShortener.repositories.AbuseReportRepository;
import com.spring.springboot.UrlShortener.repositories.MongoLinkService;
import com.spring.springboot.UrlShortener.repositories.MongoReportLinkService;
import com.spring.springboot.UrlShortener.utils.virusTotalUtils.virusTotalServices.FinalVerdict;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportLinkService {

    private final OtpService otpService;
    private final MongoLinkService mongoLinkService;
    private final LinkService linkService;
    private final MongoReportLinkService mongoReportLinkService;
    private final AbuseReportRepository abuseReportRepository;

    private final ReportLinkService self;


    public String generateOtp(String email) throws JsonProcessingException {
        return otpService.sendOtp(email);
    }

    public void tryAcceptingReport(@Valid ReportLinkRequestDto dto) {
        /*
         * Steps to perform
         * step 1: -> validate otp
         * step 2: -> extract hashedKey from the given url
         * step 3: -> check if user have already reported this report or not
         * */

//        step 1: -> validate otp
        try {
            otpService.verifyOtp(dto.getReporterEmail(), dto.getOtp());
        } catch (Exception e) {
            throw new InvalidOTPException("Invalid otp, please enter a valid otp");
        }

//        step 2:-> extract hashedKey from the given url
//        extract hashedKey from the url
//        actualUrl = url.shortener/hashedKey
//        key = hashedKey
        String hashedKey = dto.getLinkToReport().substring(14);

//        step 3: -> check if user have already reported this report or not
        boolean alreadyReported = mongoReportLinkService.isAlreadyReported(hashedKey, dto);

        if (alreadyReported) {
            String msg = dto.getReporterName() + " you have already reported this url. The url is in verification phase, we will notify you once we have confirmation";
            throw new LinkAlreadyReportedByCurrentEmailOfReporterException(msg);
        }

//        if we are reaching this lines then the link is valid and needs to report
//        step 3: -> make a call to report this url
//        calling the @Async annotated method using proxy
        self.acceptReport(hashedKey, dto);

    }

    @Async
    public void acceptReport(String hashedKey, @Valid ReportLinkRequestDto dto) {
        AbuseReport reportObject = AbuseReport.builder()
                .reporterName(dto.getReporterName())
                .reporterEmail(dto.getReporterEmail())
                .reportStatus("Pending")
                .cause(dto.getCause())
                .description(dto.getDescription())
                .hashedKeyOfLink(hashedKey)
                .createdAt(LocalDateTime.now())
                .build();

        Links link = mongoLinkService.findLinkByHashedKey(hashedKey);

        int reportCount = link.getReportCount();
        link.setReportCount(reportCount + 1);

        linkService.save(link);

//        if report count crosses a threshold-> change its status as suspicious till manual verification
        if (reportCount == 3) {
            link.setStatus(FinalVerdict.Verdict.PENDING_REVERIFICATION);
            linkService.save(link);
        }

        saveNewReport(reportObject);
    }

    public void saveNewReport(AbuseReport reportObject) {
        abuseReportRepository.save(reportObject);
    }
}
