package com.spring.springboot.UrlShortener.dto.geoIpResponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GeoInfo {
    // Basic IP and Version Info
    @JsonProperty("ip")
    private String ip;

    // Geographical Location Details
    @JsonProperty("city")
    private String city;

    @JsonProperty("region")
    private String region;

    @JsonProperty("country_name")
    private String countryName;

    @JsonProperty("postal")
    private String postal;
}
