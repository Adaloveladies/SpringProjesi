package com.adaloveladies.SpringProjesi.security;

import com.adaloveladies.SpringProjesi.exception.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/api/auth",
        "/api/auth/login",
        "/api/auth/register",
        "/swagger-ui",
        "/v3/api-docs",
        "/h2-console",
        "/error",
        "/actuator"
    );

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();

        // CORS preflight isteklerini ve public endpoint'leri kontrol et
        if (isPublicPath(requestPath) || isPreflightRequest(requestMethod)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
                logger.warn("Geçersiz veya eksik yetkilendirme başlığı. Path: {}", requestPath);
                throw new UnauthorizedException("Geçersiz veya eksik yetkilendirme başlığı.");
            }

            final String jwt = authHeader.substring(BEARER_PREFIX.length());
            final String username = jwtService.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    logger.debug("Token geçerli. Kullanıcı doğrulanıyor: {}", username);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.info("Kullanıcı başarıyla doğrulandı: {}", username);
                } else {
                    logger.warn("Geçersiz JWT token. Kullanıcı: {}", username);
                    throw new UnauthorizedException("Geçersiz veya süresi dolmuş token.");
                }
            }
        } catch (UnauthorizedException e) {
            handleUnauthorizedException(response, e);
            return;
        } catch (Exception e) {
            logger.error("Kimlik doğrulama hatası: ", e);
            handleInternalServerError(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    private boolean isPreflightRequest(String method) {
        return "OPTIONS".equalsIgnoreCase(method);
    }

    private void handleUnauthorizedException(HttpServletResponse response, UnauthorizedException e) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(String.format("{\"error\": \"%s\"}", e.getMessage()));
    }

    private void handleInternalServerError(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"error\": \"Kimlik doğrulama hatası oluştu.\"}");
    }
} 