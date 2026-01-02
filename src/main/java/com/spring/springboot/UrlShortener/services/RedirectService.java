package com.spring.springboot.UrlShortener.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spring.springboot.UrlShortener.dto.RedisDocument;
import com.spring.springboot.UrlShortener.entity.Links;
import com.spring.springboot.UrlShortener.serviceDtos.serviceResponseDtos.RedirectServiceResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedirectService {
    private final MongoTemplate mongoTemplate;
    private final RedisService redisService;
    private final LinkService linkService;

    public RedirectServiceResponseDto getActualUrlIfExists(String hash) throws JsonProcessingException {

        Long id = Base62.decode(hash);
//        as we have created a new database collection to store malicious urls separately,
//        and at creation stage, we already have sent the shortened url, so to check whether a short urls is
//        either associated with malicious url or not we have to first lookup at malicious_url_collection
//        if it found it means the shortened url is unsafe for current user, and we have to alert the user about it

//        caching -> get -> found ? return : find in database

        /*
         * minimum information to be stored in redis for url redirection as well as for analysis
         * id -> convertible from hash (for searching in Redis)
         * ownerName -> extracted from actual Link document. (for analysis)
         * actualUrl -> extracted from actual Link document (for redirection)
         */
        RedisDocument redisDocument = null;// redisService.get("link_associated_with_id_" + id.toString());
        if (redisDocument != null) {


//            actual url
            return RedirectServiceResponseDto.builder()
                    .longUrl(redisDocument.getActualUrl())
                    .status(redisDocument.getStatus())
                    .build();
        }


//        database query -> found ? set in caching (redis) : invalid url

        /* Found** -> insert in redis & push to kafka topic for analysis
         * redis needs
         * conversion from actual document to a *document* that contains minimum affordable information for redirection as well as for analysis
         * id -> convertible from hash (for searching in Redis)
         * status -> extracted from actual Link document. (for redirection)
         * ownerName -> extracted from actual Link document. (for analysis)
         * actualUrl -> extracted from actual Link document (for redirection)
         *
         *
         * analysis needs
         * ip address -> for searching in GeoIp database -> extracted from headers
         * id -> convertible from hash (for searching in Redis)
         * ownerName -> extracted from actual Link document. (for analysis)
         */

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Links linkInDb = mongoTemplate.findOne(query, Links.class);
        if (linkInDb != null) {
//            insert RedisDocument into redis for caching
            RedisDocument documentToInsertInRedis = RedisDocument.builder()
                    .actualUrl(linkInDb.getActualUrl())
                    .id(linkInDb.getId())
                    .status(linkInDb.getStatus())
                    .ownerUserName(linkInDb.getOwnerUserName())
                    .build();
            redisService.set("link_associated_with_id_" + id, documentToInsertInRedis);


//            analysis -> kafka integration
            return RedirectServiceResponseDto.builder()
                    .longUrl(linkInDb.getActualUrl())
                    .status(linkInDb.getStatus())
                    .build();
        }

//        throw new ResourceWithHashNotExistsException("Invalid url, try with correct url");
        return null;
    }

    public RedirectServiceResponseDto getUrlStatusIfExists(String hash) {

        return linkService.getLinkStatusIfExists(hash);

    }
}
