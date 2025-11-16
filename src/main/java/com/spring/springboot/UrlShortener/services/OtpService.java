package com.spring.springboot.UrlShortener.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.springboot.UrlShortener.dto.OtpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpService {


    private final SecureRandom random = new SecureRandom();
    private final StringRedisTemplate stringRedisTemplate;
    ObjectMapper mapper = new ObjectMapper();

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
            throw new RuntimeException("Error hashing OTP", e);
        }
    }


    public String sendOtp(String email) throws JsonProcessingException {
//        step 1: -> generate random 4 len numeric string
        String generatedOtp = generateFourLengthNumericOtp();

//        step 3: -> generate the hash
        String salt = "random-salt-xyz";  // generate a random one each time
        String hashedOtp = hashOtp(generatedOtp, salt);


//        step 3: -> create otpDto and store in redis
        OtpDto otpDoc = OtpDto.builder()
                .hashedOtp(hashedOtp)
                .createdAt(System.currentTimeMillis())
                .expiresAt(System.currentTimeMillis() + 300_000L) // 5 min
                .used(false)
                .build();

        String stringOtpDoc = mapper.writeValueAsString(otpDoc);

        stringRedisTemplate.opsForValue().set("otpFor:" + email, stringOtpDoc, 5, TimeUnit.MINUTES);

//        step 4: -> email the otp
        return generatedOtp;

    }

    public boolean verifyOtp(String email, String otp) throws JsonProcessingException {
        String salt = "random-salt-xyz";
        String hashedOtp = hashOtp(otp, salt);

        String stringOtpDoc = stringRedisTemplate.opsForValue().get("otpFor:" + email);

        OtpDto otpDto = mapper.readValue(stringOtpDoc, OtpDto.class);

        if (!otpDto.isUsed() && otpDto.getExpiresAt() >= System.currentTimeMillis() && hashedOtp.equals(otpDto.getHashedOtp())) {

            otpDto.setUsed(true);
            String redisOtpDoc = mapper.writeValueAsString(otpDto);

            stringRedisTemplate.opsForValue().set("otpFor:" + email, redisOtpDoc);
            return true;

        }
        return false;
    }

}
