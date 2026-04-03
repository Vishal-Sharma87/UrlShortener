package com.spring.springboot.UrlShortener.thirdPartyUtils.virusTotalUtils.virusTotalServices;

import com.fasterxml.jackson.annotation.JsonValue;
import com.spring.springboot.UrlShortener.advices.exceptions.ResourceNotExistsException;
import com.spring.springboot.UrlShortener.dto.responseDtos.VerdictDebugDto;
import com.spring.springboot.UrlShortener.enums.VerdictReason;
import com.spring.springboot.UrlShortener.thirdPartyUtils.virusTotalUtils.virusTotalDtos.AnalysisResultOfVT;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FinalVerdict {

    @ToString
    public enum Verdict {
        SAFE, SUSPICIOUS, UNVERIFIED, MALICIOUS, PENDING_REVERIFICATION;

        @JsonValue
        public String toJson() {
            return this.name();
        }
    }


    private final static Map<String, VerdictDebugDto> hashToVerdictMap = new ConcurrentHashMap<>();

    public static Verdict evaluate(AnalysisResultOfVT.Stats stats, String hash, String originalUrl) {
        int total = stats.getHarmless() + stats.getMalicious() + stats.getSuspicious()
                + stats.getUndetected() + stats.getTimeout();

        Verdict anaylysedVerdict;
        VerdictReason verdictReason;

        double harmlessRatio = (double) stats.getHarmless() / total;
        double undetectedRatio = (double) stats.getUndetected() / total;
        double maliciousRatio = (double) stats.getMalicious() / total;
        double timeoutRatio = (double) stats.getTimeout() / total;

        if (maliciousRatio > 0.05) {
            anaylysedVerdict = Verdict.MALICIOUS;
            verdictReason = VerdictReason.MALICIOUS_THRESHOLD_EXCEEDED;
        }
        else if (stats.getMalicious() > 0 || stats.getSuspicious() > 0){
            anaylysedVerdict = Verdict.MALICIOUS;
            verdictReason = VerdictReason.MALICIOUS_THRESHOLD_EXCEEDED;
        }
        else if (harmlessRatio >= 0.5 && timeoutRatio < 0.2) {
            anaylysedVerdict = Verdict.SAFE;
            verdictReason = VerdictReason.SAFE_CONSENSUS_MET;
        }
        else if (undetectedRatio > 0.6 || timeoutRatio > 0.4) {
            anaylysedVerdict = Verdict.UNVERIFIED;
            verdictReason = VerdictReason.UNDETECTED_MAJORITY;
        }
        else {
            anaylysedVerdict = Verdict.SUSPICIOUS;
            verdictReason = VerdictReason.FALLBACK_NO_CONDITION_MET;
        }

        hashToVerdictMap.put(hash, VerdictDebugDto
                                    .builder()
                        .analysedAt(LocalDateTime.now())
                        .verdict(anaylysedVerdict)
                        .harmlessRatio(harmlessRatio)
                        .maliciousRatio(maliciousRatio)
                        .undetectedRatio(undetectedRatio)
                        .timeoutRatio(timeoutRatio)
                        .verdictReason(verdictReason)
                        .originalUrl(originalUrl)
                        .shortCode(hash)
                        .totalEngines(total)
                .build());

        return anaylysedVerdict;
    }

    public static VerdictDebugDto getVerdictDetail(String hash){
        if(hashToVerdictMap.containsKey(hash)){
            return hashToVerdictMap.get(hash);
        }
        throw new ResourceNotExistsException("Verdict with given hash Not exists.");
    }



}

