package com.spring.springboot.UrlShortener.servicesTest;

import com.spring.springboot.UrlShortener.services.links.Base62;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Base62Tests {


    @Test
    @Disabled
    void checkBase62Conversion() {
//        working fine
        String hash = Base62.encode(125L);
        Long num = Base62.decode(hash);
        int a = 5;
    }
}
