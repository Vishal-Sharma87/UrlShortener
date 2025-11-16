package com.spring.springboot.UrlShortener.utils.virusTotalUtils;

import lombok.Data;

@Data
public class AnalysisIdOfVT {
    private DataObject data;

    @Data
    public static class DataObject {
        private Links links;
    }

    @Data
    public static class Links {
        //link to request a GET call to get response of scanned url
        private String self;
    }
}

