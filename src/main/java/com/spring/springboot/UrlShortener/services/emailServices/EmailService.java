package com.spring.springboot.UrlShortener.services.emailServices;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.spring.springboot.UrlShortener.dto.emailComponents.EmailDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${sendgrid.api_key}")
    private String sendgridApiKey;

    @Value("${sendgrid.verified.senders.email}")
    private String sendgridVerifiedSendersEmail;

    private String buildMail(String to, String sub, String body) throws IOException {
        Email from = new Email(sendgridVerifiedSendersEmail);

        Email recipientsEmail = new Email(to);

        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, sub, recipientsEmail, content);
        return mail.build();
    }

    private Response getSendGridResponse(EmailDto dto) throws IOException {

        String mail = buildMail(dto.getTo(), dto.getSubject(), dto.getContent());

        SendGrid sg = new SendGrid(sendgridApiKey);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail);
        return sg.api(request);
    }

    public void sendEmail(EmailDto dto) throws IOException {
//        some functionality

        Response sendGridResponse = getSendGridResponse(dto);
//        TODO different functionality according to the response code

    }
}
