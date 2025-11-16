package com.spring.springboot.UrlShortener.links;

import com.spring.springboot.UrlShortener.services.LinkService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.aot.DisabledInAotMode;

@SpringBootTest
public class LinkCreationTests {

    @Autowired
    private LinkService linkService;

    @Test
    @Disabled
    public void testingCreationOfLink(){
    }

}
