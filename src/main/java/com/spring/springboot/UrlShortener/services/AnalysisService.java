package com.spring.springboot.UrlShortener.services;


import com.spring.springboot.UrlShortener.dto.TrackPayloadDto;
import com.spring.springboot.UrlShortener.dto.geoIpResponses.GeoInfo;
import com.spring.springboot.UrlShortener.entity.LinkInformation;
import com.spring.springboot.UrlShortener.entity.Links;
import com.spring.springboot.UrlShortener.enums.Browser;
import com.spring.springboot.UrlShortener.enums.Device;
import com.spring.springboot.UrlShortener.enums.OperatingSystem;
import com.spring.springboot.UrlShortener.model.LinkAnalysisDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final KafkaTemplate<String, LinkAnalysisDto> kafkaTemplate;
    private final WebClient webClient;
    private final LinkService linkService;
    private final LinkInformationService linkInformationService;


    private final String genericApi = "ipapi.co/{ip}/json";


    private String getClientIp(HttpServletRequest request) {

        String[] headers = {
                "X-Forwarded-For",
                "X-Real-IP",
                "CF-Connecting-IP",
                "Forwarded"
        };

        for (String header : headers) {
            String value = request.getHeader(header);
            if (value != null && !value.isBlank()) {
                return value.split(",")[0].trim();
            }
        }

        return request.getRemoteAddr();
    }


    public void sendDataToKafka(TrackPayloadDto dto, HttpServletRequest request) {

        String ip = getClientIp(request);

        LinkAnalysisDto linkAnalysisDto = LinkAnalysisDto.builder()
                .entityIp(ip)
                .trackPayloadDto(dto)
                .build();
        kafkaTemplate.send("URL_SHORTENER_existed_link_analysis", linkAnalysisDto);
    }

    public void analyze(LinkAnalysisDto linkAnalysisDto) {
        /*
         */

//        step1: check if shortHash exists or not, if not then return
        String hash = linkAnalysisDto.getTrackPayloadDto().getShortHash();
        Links linkInDb = linkService.getLinkByHash(hash);


        if (hash == null || hash.isBlank() || linkInDb == null) return;


//        step2: extract ip
        String ip = linkAnalysisDto.getEntityIp();

//        step3: get geoIpResponses for the extracted ip address
        GeoInfo geoInfo = getGeoIpResponse(ip);

        int width;
        if (linkAnalysisDto.getTrackPayloadDto().getViewportWidth() == null
                && linkAnalysisDto.getTrackPayloadDto().getScreenWidth() == null) {
            width = 0;
        } else {
            width = linkAnalysisDto.getTrackPayloadDto().getViewportWidth() == null
                    ? linkAnalysisDto.getTrackPayloadDto().getScreenWidth()
                    : linkAnalysisDto.getTrackPayloadDto().getViewportWidth();
        }

//        step4: Browser categorization based on userAgent
        Browser browser = categoriseBrowserBasedOnUserAgent(linkAnalysisDto.getTrackPayloadDto().getUserAgent());

//        step5: OS categorization
        OperatingSystem os = categoriseOperatingSystemBasedOnUserAgent(linkAnalysisDto.getTrackPayloadDto().getUserAgent());

//        step6 :device categorization based on width, if device is non-mobile then -> device recategorization because resized window can result in wrong device conclusion
        Device device = categoriseDeviceBasedOnWidthAndOperatingSystem(width, os);

//        step7: increment short link click count and save
        linkInDb.setClickCount(linkInDb.getClickCount() + 1);
        linkService.save(linkInDb);

//        step8:  create a POJO and save into db
        LinkInformation.ClickerDeviceInfo deviceInfo = LinkInformation.ClickerDeviceInfo.builder()
                .browser(browser)
                .device(device)
                .operatingSystem(os)
                .build();

        LinkInformation linkInformation = LinkInformation.builder()
                .associatedShortHash(hash)
                .entityGeoInformation(geoInfo)
                .timeZone(linkAnalysisDto.getTrackPayloadDto().getTimezone())
                .timeOfClick(Instant.now())
                .deviceInfo(deviceInfo)
                .build();

        linkInformationService.save(linkInformation);


    }

    private OperatingSystem categoriseOperatingSystemBasedOnUserAgent(String userAgent) {
        if (userAgent.contains("Windows")) return OperatingSystem.WINDOWS;
        if (userAgent.contains("Mac")) return OperatingSystem.MAC;
        if (userAgent.contains("Android")) return OperatingSystem.ANDROID;
        if (userAgent.contains("iPhone")) return OperatingSystem.IOS;
        if (userAgent.contains("Linux")) return OperatingSystem.LINUX;
        else return OperatingSystem.OTHERS;

    }

    private Browser categoriseBrowserBasedOnUserAgent(String userAgent) {
        if (userAgent.contains("Edg")) return Browser.MICROSOFT_EDGE;
        if (userAgent.contains("Firefox")) return Browser.FIREFOX;
        if (userAgent.contains("Chrome")) return Browser.CHROME;
        if (userAgent.contains("Safari")) return Browser.SAFARI;
        else return Browser.OTHERS;

    }

    private Device categoriseDeviceBasedOnWidthAndOperatingSystem(int width, OperatingSystem os) {

        if (os == null || width <= 0) {
            return Device.OTHERS;
        }

        switch (os) {
            case ANDROID, IOS:
                if (width <= 600) {
                    return Device.MOBILE;
                }
                if (width <= 1024) {
                    return Device.TABS;
                }
                return Device.OTHERS;


            case WINDOWS, MAC, LINUX:
                if (width <= 500) {
                    // Resized desktop window
                    return Device.DESKTOPS;
                }
                if (width <= 1366) {
                    return Device.LAPTOPS;
                }
                return Device.DESKTOPS;

            default:
                return Device.OTHERS;
        }
    }


    private GeoInfo getGeoIpResponse(String ip) {
        String uriToCall = genericApi.replace("{ip}", ip);
        try {
            return webClient.get()
                    .uri(new URI(uriToCall))
                    .retrieve()
                    .bodyToMono(GeoInfo.class)
                    .block();
        } catch (URISyntaxException e) {
            log.error("Something went wrong while converting the api for GeoIp response. Exception: {}", e.getMessage());
            return null;
        } catch (Exception ex) {
            log.error("Something went wrong while fetching GeoIp response. Exception: {}", ex.getMessage());
            return null;
        }
    }
}
