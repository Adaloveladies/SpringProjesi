package com.adaloveladies.SpringProjesi.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Configuration
public class IpRateLimitConfig {

    @Value("${rate.limit.requests:50}")
    private int maxRequests;

    @Value("${rate.limit.duration:60}")
    private int durationInSeconds;

    @Value("${rate.limit.cleanup.period:3600}")
    private int cleanupPeriodInSeconds;

    private final Map<String, Bucket> ipBuckets = new ConcurrentHashMap<>();
    private final Map<String, Long> lastAccessTime = new ConcurrentHashMap<>();
    
    @Bean
    public Map<String, Bucket> ipBuckets() {
        return ipBuckets;
    }
    
    public Bucket resolveIpBucket(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            throw new IllegalArgumentException("IP adresi boş olamaz");
        }
        lastAccessTime.put(ip, System.currentTimeMillis());
        return ipBuckets.computeIfAbsent(ip, this::newIpBucket);
    }
    
    private Bucket newIpBucket(String ip) {
        Refill refill = Refill.intervally(maxRequests, Duration.ofSeconds(durationInSeconds));
        Bandwidth limit = Bandwidth.classic(maxRequests, refill);
        
        return Bucket4j.builder()
                .addLimit(limit)
                .build();
    }

    @Scheduled(fixedRateString = "${rate.limit.cleanup.period:3600000}")
    public void cleanupInactiveBuckets() {
        long currentTime = System.currentTimeMillis();
        ipBuckets.entrySet().removeIf(entry -> {
            String ip = entry.getKey();
            Long lastAccess = lastAccessTime.get(ip);
            if (lastAccess == null) {
                return true;
            }
            boolean shouldRemove = currentTime - lastAccess > TimeUnit.SECONDS.toMillis(cleanupPeriodInSeconds);
            if (shouldRemove) {
                lastAccessTime.remove(ip);
            }
            return shouldRemove;
        });
    }

    public boolean tryConsume(String ip) {
        Bucket bucket = resolveIpBucket(ip);
        return bucket.tryConsume(1);
    }

    public long getAvailableTokens(String ip) {
        Bucket bucket = resolveIpBucket(ip);
        return bucket.getAvailableTokens();
    }

    public long getResetTime(String ip) {
        Bucket bucket = resolveIpBucket(ip);
        if (bucket == null) {
            return System.currentTimeMillis() + durationInSeconds * 1000L;
        }
        return lastAccessTime.getOrDefault(ip, System.currentTimeMillis()) + durationInSeconds * 1000L;
    }
} 