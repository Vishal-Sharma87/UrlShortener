package com.spring.springboot.UrlShortener.utils.kafkaUtils.consumer;

import com.spring.springboot.UrlShortener.entity.Links;
import com.spring.springboot.UrlShortener.entity.UrlUser;
import com.spring.springboot.UrlShortener.model.LinkAnalysisDto;
import com.spring.springboot.UrlShortener.model.LinkCreationDto;
import com.spring.springboot.UrlShortener.services.LinkService;
import com.spring.springboot.UrlShortener.services.UserService;
import com.spring.springboot.UrlShortener.utils.virusTotalUtils.virusTotalServices.FinalVerdict;
import com.spring.springboot.UrlShortener.utils.virusTotalUtils.virusTotalServices.VirusTotalService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ConsumerLinkCreationEvent {

    private final UserService userService;

    private final VirusTotalService vtService;

    private final LinkService linkService;

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
//                            TODO send an email stating that the user have exceeded the bearable limit of creation of malicious url
//                             and is blocked and will never be able to short the url
                        }
//                        TODO send a confirmation email stating that the url is found suspicious,
//                         and and doing again so can cause permanent ban from accessing the site
//
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
