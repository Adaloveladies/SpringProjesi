package com.adaloveladies.SpringProjesi.interceptor;

import com.adaloveladies.SpringProjesi.config.IpRateLimitConfig;
import com.adaloveladies.SpringProjesi.exception.TooManyRequestsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitInterceptor.class);
    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
        "/swagger-ui",
        "/v3/api-docs",
        "/h2-console",
        "/error",
        "/api/auth/login",
        "/api/auth/register"
    );

    private static final String RETRY_AFTER_HEADER = "Retry-After";
    private static final String RATE_LIMIT_REMAINING_HEADER = "X-Rate-Limit-Remaining";
    private static final String RATE_LIMIT_RESET_HEADER = "X-Rate-Limit-Reset";
    private static final int RETRY_AFTER_SECONDS = 60;

    private final IpRateLimitConfig ipRateLimitConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();
        
        // CORS preflight isteklerini ve hariç tutulan endpoint'leri kontrol et
        if (isExcludedPath(requestPath) || isPreflightRequest(requestMethod)) {
            return true;
        }

        try {
            String clientIp = getClientIp(request);
            logger.debug("Rate limit kontrolü yapılıyor. IP: {}, Path: {}", clientIp, requestPath);
            
            if (ipRateLimitConfig.tryConsume(clientIp)) {
                long remainingTokens = ipRateLimitConfig.getAvailableTokens(clientIp);
                long resetTime = ipRateLimitConfig.getResetTime(clientIp);
                
                // Rate limit başlıklarını ayarla
                setRateLimitHeaders(response, remainingTokens, resetTime);
                return true;
            }

            // Rate limit aşıldı
            handleRateLimitExceeded(response);
            return false;
        } catch (TooManyRequestsException e) {
            handleTooManyRequests(response, e);
            return false;
        } catch (Exception e) {
            logger.error("Rate limit hatası: ", e);
            handleInternalServerError(response);
            return false;
        }
    }

    private boolean isExcludedPath(String path) {
        return EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
    }

    private boolean isPreflightRequest(String method) {
        return "OPTIONS".equalsIgnoreCase(method);
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }

    private void setRateLimitHeaders(HttpServletResponse response, long remainingTokens, long resetTime) {
        response.setHeader(RATE_LIMIT_REMAINING_HEADER, String.valueOf(remainingTokens));
        response.setHeader(RATE_LIMIT_RESET_HEADER, String.valueOf(resetTime));
    }

    private void handleRateLimitExceeded(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setHeader(RETRY_AFTER_HEADER, String.valueOf(RETRY_AFTER_SECONDS));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.format(
            "{\"error\": \"Çok fazla istek gönderildi. Lütfen %d saniye sonra tekrar deneyin.\"}", 
            RETRY_AFTER_SECONDS
        ));
    }

    private void handleTooManyRequests(HttpServletResponse response, TooManyRequestsException e) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setHeader(RETRY_AFTER_HEADER, String.valueOf(RETRY_AFTER_SECONDS));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.format("{\"error\": \"%s\"}", e.getMessage()));
    }

    private void handleInternalServerError(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"Rate limit kontrolü sırasında bir hata oluştu.\"}");
    }
} 