package com.spring.springboot.UrlShortener.services;

import com.spring.springboot.UrlShortener.dto.LoginRequest;
import com.spring.springboot.UrlShortener.dto.SignupRequest;
import com.spring.springboot.UrlShortener.entity.UrlUser;
import com.spring.springboot.UrlShortener.exceptions.UserWithUserNameAlreadyExitsException;
import com.spring.springboot.UrlShortener.repositories.UserRepository;
import com.spring.springboot.UrlShortener.services.jwtAuth.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // Business logic for signup
    public String registerUser(SignupRequest request) {
        UrlUser userInDb = userRepository.findUserByUserName(request.getUserName());
        if (userInDb != null)
            throw new UserWithUserNameAlreadyExitsException("User with userName :" + request.getUserName() + " already exists");


//        TODO verify the otp
        UrlUser user = UrlUser.builder()
                .email(request.getEmail())
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of("USER"))
                .creationDate(new Date())
                .maliciousUrlsCreatedCount(0)
                .build();

        userRepository.save(user);
//        "User registered successfully!"
        return jwtService.generateJwt(request.getUserName());
    }

    // Business logic for login
    public String loginUser(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                )
        );
        return jwtService.generateJwt(request.getUserName());
    }
}
