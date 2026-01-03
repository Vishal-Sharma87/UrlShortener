package com.spring.springboot.UrlShortener.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackPayloadDto {

    // URL related
    private String shortHash;

    // Screen information
//    for current project not considering height because the main categorization is based on width not height
    private Integer screenWidth;

    // Browser viewport
    private Integer viewportWidth;

    // Browser details
    private String userAgent;
    private String timezone;
}

