package com.spring.springboot.UrlShortener.VTServices;

import com.spring.springboot.UrlShortener.thirdPartyUtils.virusTotalUtils.virusTotalServices.VirusTotalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ScanUrlTest {

    @Autowired
    private VirusTotalService vtService;


    @Test
    void scanUrl() {

        vtService.scanUrl("https://instagram.com");

        int a = 5;
    }
}
