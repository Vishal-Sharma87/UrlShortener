package com.spring.springboot.UrlShortener.servicesTest.reportLink;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spring.springboot.UrlShortener.services.auth.OtpService;
import com.spring.springboot.UrlShortener.services.ReportLinkService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class ReportLinkServiceTests {

    @Autowired
    private ReportLinkService reportLinkService;

    @Autowired
    private OtpService otpService;


    @Test
    @Disabled
    void testAcceptingReport() throws JsonProcessingException {

    }

}
