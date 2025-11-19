package com.spring.springboot.UrlShortener.utils.virusTotalUtils.virusTotalServices;

import com.spring.springboot.UrlShortener.utils.virusTotalUtils.AnalysisResultOfVT;
import lombok.ToString;

public class FinalVerdict {

    public static Verdict evaluate(AnalysisResultOfVT.Stats stats) {
        int total = stats.getHarmless() + stats.getMalicious() + stats.getSuspicious()
                + stats.getUndetected() + stats.getTimeout();

        double harmlessRatio = (double) stats.getHarmless() / total;
        double undetectedRatio = (double) stats.getUndetected() / total;
        double maliciousRatio = (double) stats.getMalicious() / total;
        double timeoutRatio = (double) stats.getTimeout() / total;

        if (maliciousRatio > 0.05) return Verdict.MALICIOUS;
        if (stats.getMalicious() > 0 || stats.getSuspicious() > 0) return Verdict.MALICIOUS;

        if (harmlessRatio >= 0.5 && timeoutRatio < 0.2) return Verdict.SAFE;
        if (undetectedRatio > 0.6 || timeoutRatio > 0.4) return Verdict.UNVERIFIED;

        return Verdict.SUSPICIOUS;
    }

    @ToString
    public enum Verdict {
        SAFE, SUSPICIOUS, UNVERIFIED, MALICIOUS, PENDING_REVERIFICATION
    }

}

