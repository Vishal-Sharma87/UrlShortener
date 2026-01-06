package com.spring.springboot.UrlShortener.services.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.springboot.UrlShortener.dto.RedisDtos.OtpRedisDocDto;
import com.spring.springboot.UrlShortener.dto.emailComponents.EmailContentBuilder;
import com.spring.springboot.UrlShortener.dto.emailComponents.EmailDto;
import com.spring.springboot.UrlShortener.services.emailServices.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {


    private static final String REDIS_KEY_FOR_EMAIL_OTP = "otpFor:";
    private final SecureRandom random = new SecureRandom();
    private final StringRedisTemplate stringRedisTemplate;
    private final EmailContentBuilder emailContentBuilder;
    private final EmailService emailService;
    private final ObjectMapper mapper;

    //    generate a random numeric string of length 4
    private String generateFourLengthNumericOtp() {
        StringBuilder sb = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(10)); // 0–9
        }
        return sb.toString();
    }

    private String hashOtp(String otp, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String combined = otp + salt;
            byte[] hashBytes = digest.digest(combined.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            log.error("Something went wrong while hashing the OTP. Exception: {}", e.getMessage());
            throw new IllegalStateException("SHA-256 not available");
        }
    }


    public void sendOtp(String email) throws JsonProcessingException {
//        step 1: -> generate random 4 len numeric string
        String generatedOtp = generateFourLengthNumericOtp();

//        step 3: -> generate the hash
        String salt = "random-salt-xyz";  // generate a random one each time
        String hashedOtp = hashOtp(generatedOtp, salt);


//        step 3: -> create otpDto and store in redis
        OtpRedisDocDto otpDoc = OtpRedisDocDto.builder()
                .hashedOtp(hashedOtp)
                .createdAt(System.currentTimeMillis())
                .expiresAt(System.currentTimeMillis() + 1000 * 60 * 8) // 8 min
                .used(false)
                .build();

        String stringOtpDoc = mapper.writeValueAsString(otpDoc);

        stringRedisTemplate.opsForValue().set(REDIS_KEY_FOR_EMAIL_OTP + email, stringOtpDoc, 8, TimeUnit.MINUTES);


        EmailDto emailDto = emailContentBuilder.getEmilDtoWithOtpContent(generatedOtp, email);
        try {
            emailService.sendEmail(emailDto);
        } catch (IOException e) {
            log.warn("Something went wrong with Sendgrid api while sending the otp for the email: {}. Exception: {}", email, e.getMessage());
        }
    }


    public boolean isValidOtp(String email, String otp) throws JsonProcessingException {
        String salt = "random-salt-xyz";
        String hashedOtp = hashOtp(otp, salt);

        String stringOtpDoc = stringRedisTemplate.opsForValue().get(REDIS_KEY_FOR_EMAIL_OTP + email);
        if (stringOtpDoc == null) return false;

        OtpRedisDocDto otpRedisDocDto = mapper.readValue(stringOtpDoc, OtpRedisDocDto.class);

        if (!otpRedisDocDto.isUsed() && otpRedisDocDto.getExpiresAt() >= System.currentTimeMillis() && hashedOtp != null && hashedOtp.equals(otpRedisDocDto.getHashedOtp())) {

            stringRedisTemplate.delete(REDIS_KEY_FOR_EMAIL_OTP + email);
            return true;

        }
        return false;
    }

}
