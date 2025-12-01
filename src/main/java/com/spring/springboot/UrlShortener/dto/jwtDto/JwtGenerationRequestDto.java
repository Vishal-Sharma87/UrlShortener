package com.spring.springboot.UrlShortener.dto.jwtDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtGenerationRequestDto {

    private String userName;

}
