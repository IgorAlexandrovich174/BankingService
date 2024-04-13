package com.example.bankingservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class SecretKeyConfig {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecretKey getJwtSecret() {
        if (jwtSecret == null || jwtSecret.isEmpty()) {
            throw new IllegalStateException("jwt.secret is not set");
        }
        return new SecretKeySpec(Base64.getDecoder().decode(jwtSecret), "AES");
    }
}
