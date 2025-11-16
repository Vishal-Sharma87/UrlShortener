package com.spring.springboot.UrlShortener.entity;

import com.spring.springboot.UrlShortener.utils.virusTotalUtils.virusTotalServices.FinalVerdict;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "linksOfUrlShortener")
@Data
@Builder
public class Links {

    //    base10 value of counter variable stored in redis
    @Id
    private Long id;


    private String actualUrl;

    //     base62 conversion of counter variable stored in redis
    private String hashedKey;

    //    owner of link, for analysis purposes
    @Indexed
    private String ownerUserName;

    //    a separate mongodb collection to store particular URLs analysis
    @DBRef
    private List<LinkInformation> linkInformation;

    //    date of creation of a url
    private Date linkCreationTime;

    //   current status of the url either safe, malicious or suspicious
    private FinalVerdict.Verdict status;


//    url reports
    private List<AbuseReport> abuseReports;

    //    if someone report the link as unsafe ,then we will increment the counter
//    TODO if it hits a predefined threshold then will send a mail to owner to take action on it
    private int reportCount;

    //    for analysis purposes
    private Date firstReportedTime;

    private Date lastReportedTime;


}
