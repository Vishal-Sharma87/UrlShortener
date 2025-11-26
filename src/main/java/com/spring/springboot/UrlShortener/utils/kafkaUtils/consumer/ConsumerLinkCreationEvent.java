package com.spring.springboot.UrlShortener.utils.kafkaUtils.consumer;

import com.spring.springboot.UrlShortener.dto.emailComponents.EmailContentBuilder;
import com.spring.springboot.UrlShortener.dto.emailComponents.EmailDto;
import com.spring.springboot.UrlShortener.entity.Links;
import com.spring.springboot.UrlShortener.entity.UrlUser;
import com.spring.springboot.UrlShortener.exceptions.SendgridEmailFailedException;
import com.spring.springboot.UrlShortener.model.LinkAnalysisDto;
import com.spring.springboot.UrlShortener.model.LinkCreationDto;
import com.spring.springboot.UrlShortener.services.LinkService;
import com.spring.springboot.UrlShortener.services.UserService;
import com.spring.springboot.UrlShortener.services.emailServices.EmailService;
import com.spring.springboot.UrlShortener.utils.virusTotalUtils.virusTotalServices.FinalVerdict;
import com.spring.springboot.UrlShortener.utils.virusTotalUtils.virusTotalServices.VirusTotalService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ConsumerLinkCreationEvent {

    private final UserService userService;

    private final VirusTotalService vtService;

    private final LinkService linkService;

    private final EmailService emailService;

    private final EmailContentBuilder emailContentBuilder;

    @KafkaListener(
            topics = "URL_SHORTENER_link_creation",
            groupId = "link-creation-group",
            containerFactory = "linkCreationKafkaListenerContainerFactory"
    )
    public void createNewLink(LinkCreationDto linkCreationDto) {
        // Step 0: Fetch user from DB
        UrlUser userInDb = userService.getUserByUserName(linkCreationDto.getOwnerUserName());


        // Step 1: Scan URL asynchronously (non-blocking)
        vtService.scanUrl(linkCreationDto.getLongUrl())
                .subscribe(verdict -> {
                    // ⚡ This block runs when scan completes
                    // Step 2: Proceed with URL creation or mark malicious
                    // Create short URL in DB
                    Links createdLink = Links.builder()
                            .id(linkCreationDto.getId())
                            .actualUrl(linkCreationDto.getLongUrl())
                            .hashedKey(linkCreationDto.getGeneratedHash())
                            .linkCreationTime(new Date())
                            .status(verdict)
                            .ownerUserName(linkCreationDto.getOwnerUserName())
                            .linkInformation(new ArrayList<>())
                            .abuseReports(new ArrayList<>())
                            .firstReportedTime(null)
                            .lastReportedTime(null)
                            .reportCount(0)
                            .build();
                    linkService.saveNewLink(createdLink);

//                    userInDb.getLinks().add(createdLink);
//                    userService.save(userInDb);

                    if (verdict == FinalVerdict.Verdict.MALICIOUS || verdict == FinalVerdict.Verdict.SUSPICIOUS) {
                        // save this entry into the user, if the count of maliciousUrls touches a predefined threshold then
                        // block the user from further usage of our services
                        if (userInDb.getMaliciousUrlsCreatedCount() + 1 >= 5) {
//                             send an email stating that the user have exceeded the bearable limit of creation of malicious url
//                             and is blocked and will never be able to short the url

                            EmailDto permanentBlockNotification = emailContentBuilder.getEmailDtoWithContentPermanentBlockNotification(userInDb.getEmail());
                            try {
                                emailService.sendEmail(permanentBlockNotification);
                            } catch (IOException e) {
                                throw new SendgridEmailFailedException("Something went wrong and email for notification about permanent ban due to exceeding threshold failed. Exception: " + e.getMessage());

                            }
                        }
//                        send a confirmation email stating that the url is found suspicious,
//                          and doing again so can cause permanent ban from accessing the site
                        EmailDto suspiciousUrlWarning = emailContentBuilder.getEmailDtoWithContentSuspiciousUrlWarning(linkCreationDto.getLongUrl(), userInDb.getEmail());
                        try {
                            emailService.sendEmail(suspiciousUrlWarning);
                        } catch (IOException e) {
                            throw new SendgridEmailFailedException("Something went wrong and email for notification about link is found suspicious failed. Exception: " + e.getMessage());
                        }
//
                    } else {
//                             send a confirmation email stating that the url is shortened and is now available for usage

                        EmailDto urlShortenedConfirmation = emailContentBuilder.getEmailDtoWithContentUrlShortenedConfirmation(linkCreationDto.getGeneratedHash(), linkCreationDto.getLongUrl(), userInDb.getEmail());

                        try {
                            emailService.sendEmail(urlShortenedConfirmation);
                        } catch (IOException e) {
                            throw new SendgridEmailFailedException("Something went wrong and email for notification about successful short link creation failed. Exception: " + e.getMessage());

                        }
                    }

                });
    }

    // For LinkAnalysis topic
    @KafkaListener(
            topics = "URL_SHORTENER_existed_link_analysis",
            groupId = "link-analysis-group",
            containerFactory = "linkAnalysisKafkaListenerContainerFactory" // Add this
    )
    public void analysis(LinkAnalysisDto linkAnalysisDto) {
        System.out.println("Received LinkAnalysisDto: " + linkAnalysisDto);
    }
}
