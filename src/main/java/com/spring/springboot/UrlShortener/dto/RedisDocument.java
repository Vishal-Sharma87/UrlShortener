package com.spring.springboot.UrlShortener.dto;

import com.spring.springboot.UrlShortener.utils.virusTotalUtils.virusTotalServices.FinalVerdict;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedisDocument {
    private Long id;
    private String actualUrl;
    private FinalVerdict.Verdict status;
    private String ownerUserName;
}
