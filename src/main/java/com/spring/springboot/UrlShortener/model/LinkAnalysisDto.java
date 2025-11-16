package com.spring.springboot.UrlShortener.model;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LinkAnalysisDto {

    private String key;
    private String value;
}
