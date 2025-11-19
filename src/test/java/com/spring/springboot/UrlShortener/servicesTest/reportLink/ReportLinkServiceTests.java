package com.spring.springboot.UrlShortener.servicesTest.reportLink;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spring.springboot.UrlShortener.dto.ReportLinkRequestDto;
import com.spring.springboot.UrlShortener.services.OtpService;
import com.spring.springboot.UrlShortener.services.ReportLinkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
public class ReportLinkServiceTests {

    @Autowired
    private ReportLinkService reportLinkService;

    @Autowired
    private OtpService otpService;


    @Test
    void testAcceptingReport() throws JsonProcessingException {

        String otp = otpService.sendOtp("test3@otp.com");

        ReportLinkRequestDto dto = ReportLinkRequestDto.builder()
                .linkToReport("url.shortener/5GTM82")
                .reporterName("user3")
                .reporterEmail("test3@otp.com")
                .otp(otp)
                .cause(new ArrayList<>())
                .description("Testing")
                .build();
        reportLinkService.tryAcceptingReport(dto);

        int a = 5;
    }

}
