package com.spring.springboot.UrlShortener.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI urlShortenerOpenAPI() {

        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("URL Shortener API")
                        .description("""
                                Backend-driven URL Shortener providing:
                                - Secure authentication (OTP + JWT)
                                - Short URL generation
                                - Safety-based redirection
                                - Analytics tracking
                                - Community-driven abuse reporting
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Vishal Sharma")
                        )
                )
                // 🔐 Tell Swagger JWT is used
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new io.swagger.v3.oas.models.Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name("Authorization")
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }
}
