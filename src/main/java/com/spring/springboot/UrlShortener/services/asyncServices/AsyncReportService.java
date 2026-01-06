package com.spring.springboot.UrlShortener.services.asyncServices;

import com.spring.springboot.UrlShortener.dto.requestDtos.ReportLinkRequestDto;
import com.spring.springboot.UrlShortener.dto.emailComponents.EmailContentBuilder;
import com.spring.springboot.UrlShortener.dto.emailComponents.EmailDto;
import com.spring.springboot.UrlShortener.entity.AbuseReport;
import com.spring.springboot.UrlShortener.entity.Links;
import com.spring.springboot.UrlShortener.advices.exceptions.SendgridEmailFailedException;
import com.spring.springboot.UrlShortener.repositories.AbuseReportRepository;
import com.spring.springboot.UrlShortener.repositories.MongoLinkService;
import com.spring.springboot.UrlShortener.services.links.LinkService;
import com.spring.springboot.UrlShortener.services.emailServices.EmailService;
import com.spring.springboot.UrlShortener.thirdPartyUtils.virusTotalUtils.virusTotalServices.FinalVerdict;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AsyncReportService {

    private final MongoLinkService mongoLinkService;
    private final LinkService linkService;
    private final AbuseReportRepository abuseReportRepository;
    private final EmailContentBuilder emailContentBuilder;
    private final EmailService emailService;


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

        abuseReportRepository.save(reportObject);
//        send a confirmation mail that we have accepted his report and will notify after confirmation
        EmailDto emailDtoWithSuccessfulReportContent = emailContentBuilder.getEmailDtoWithSuccessfulReportContent(dto.getLinkToReport(), dto.getReporterEmail());
        try {
            emailService.sendEmail(emailDtoWithSuccessfulReportContent);
        } catch (IOException e) {
            throw new SendgridEmailFailedException("Something went wrong and email for successful report failed. Exception: " + e.getMessage());
        }

    }
}
