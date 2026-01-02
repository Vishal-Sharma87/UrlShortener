package com.spring.springboot.UrlShortener.geoIpTests;

import com.spring.springboot.UrlShortener.dto.geoIpResponses.GeoInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestResponseFromGeoIpForIpAddress {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void test() throws IOException, URISyntaxException {


//        URL url = new URL("https://ipapi.co/8.8.8.8/json");

        String ip = "8.8.4.4";

        String url = "https://ipapi.co/{ip}/json";
        String replace = url.replace("{ip}", ip);

        URI uri = new URI("https://ipapi.co/8.8.8.8/json");
        ResponseEntity<GeoInfo> forEntity = restTemplate.getForEntity(uri, GeoInfo.class);



        int a = 5;


    }

}
