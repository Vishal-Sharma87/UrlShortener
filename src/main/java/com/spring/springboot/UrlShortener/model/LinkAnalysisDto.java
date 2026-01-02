package com.spring.springboot.UrlShortener.model;


import com.spring.springboot.UrlShortener.dto.TrackPayloadDto;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LinkAnalysisDto {

    private String entityIp;
    private TrackPayloadDto trackPayloadDto;
}
