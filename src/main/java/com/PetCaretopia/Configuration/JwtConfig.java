package com.PetCaretopia.Configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class JwtConfig {
    @Value("${spring.jwt.secretKey}")
    private String secretKey;
    @Value("${spring.jwt.expiration}")
    private long expiration;
}
