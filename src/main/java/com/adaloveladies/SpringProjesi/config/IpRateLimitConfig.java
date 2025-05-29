package com.adaloveladies.SpringProjesi.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class IpRateLimitConfig {

    private final Map<String, Bucket> ipBuckets = new ConcurrentHashMap<>();
    
    @Bean
    public Map<String, Bucket> ipBuckets() {
        return ipBuckets;
    }
    
    public Bucket resolveIpBucket(String ip) {
        return ipBuckets.computeIfAbsent(ip, this::newIpBucket);
    }
    
    private Bucket newIpBucket(String ip) {
        // Her IP için 1 dakikada 50 istek
        Refill refill = Refill.intervally(50, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(50, refill);
        
        return Bucket4j.builder()
                .addLimit(limit)
                .build();
    }
} 