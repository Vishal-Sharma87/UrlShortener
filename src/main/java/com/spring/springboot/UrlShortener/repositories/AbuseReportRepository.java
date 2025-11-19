package com.spring.springboot.UrlShortener.repositories;

import com.spring.springboot.UrlShortener.entity.AbuseReport;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AbuseReportRepository extends MongoRepository<AbuseReport, ObjectId> {
}
