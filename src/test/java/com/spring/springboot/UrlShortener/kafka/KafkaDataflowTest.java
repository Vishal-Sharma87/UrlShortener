package com.spring.springboot.UrlShortener.kafka;

import com.spring.springboot.UrlShortener.model.LinkAnalysisDto;
import com.spring.springboot.UrlShortener.model.LinkCreationDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
class KafkaDataflowTest {

    @Autowired
    private KafkaTemplate<String, LinkCreationDto> template1;
    @Autowired
    private KafkaTemplate<String, LinkAnalysisDto> template2;

    @Test
    @Disabled
    void sendDataToKafkaTopic() throws Exception {



        LinkAnalysisDto analysisDto = LinkAnalysisDto.builder()
                .key("a")
                .value("b")
                .build();

        // Send message

//        template1.send("URL_SHORTENER_link_creation", doc);
        template2.send("URL_SHORTENER_existed_link_analysis", analysisDto);


        int a = 5;
    }
}
