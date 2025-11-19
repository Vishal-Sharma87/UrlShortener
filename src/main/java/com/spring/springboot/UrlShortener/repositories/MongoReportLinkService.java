package com.spring.springboot.UrlShortener.repositories;

import com.spring.springboot.UrlShortener.dto.ReportLinkRequestDto;
import com.spring.springboot.UrlShortener.entity.AbuseReport;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongoReportLinkService {

    private final MongoTemplate mongoTemplate;

    public boolean isAlreadyReported(String hashedKey, @Valid ReportLinkRequestDto dto) {
        Query query = new Query();
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("hashedKeyOfLink").is(hashedKey),
                Criteria.where("reporterEmail").is(dto.getReporterEmail())
        );

        query.addCriteria(criteria);

//        we will stop finding in db when we find first report entry
        query.limit(1);

        return !mongoTemplate.find(query, AbuseReport.class).isEmpty();
    }
}
