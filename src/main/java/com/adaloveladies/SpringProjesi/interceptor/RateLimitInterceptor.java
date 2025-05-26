package com.adaloveladies.SpringProjesi.interceptor;

import com.adaloveladies.SpringProjesi.exception.TooManyRequestsException;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final Bucket bucket;

    public RateLimitInterceptor(Bucket bucket) {
        this.bucket = bucket;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (bucket.tryConsume(1)) {
            return true;
        }
        throw new TooManyRequestsException("Çok fazla istek gönderildi. Lütfen daha sonra tekrar deneyin.");
    }
} 