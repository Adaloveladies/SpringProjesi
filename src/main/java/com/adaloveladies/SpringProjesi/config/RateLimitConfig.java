package com.adaloveladies.SpringProjesi.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {

    @Bean
    public Bucket createNewBucket() {
        // Her kullanıcı için dakikada 50 istek limiti
        Bandwidth limit = Bandwidth.simple(50, Duration.ofMinutes(1));
        
        return Bucket4j.builder()
                .addLimit(limit)
                .build();
    }
} 