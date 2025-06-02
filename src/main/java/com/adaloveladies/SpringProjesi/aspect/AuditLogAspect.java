package com.adaloveladies.SpringProjesi.aspect;

import com.adaloveladies.SpringProjesi.audit.AuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditLogAspect {

    private final AuditLogService auditLogService;

    @Around("@annotation(com.adaloveladies.SpringProjesi.annotation.AuditLog)")
    public Object logAudit(ProceedingJoinPoint joinPoint) throws Throwable {
        String kullaniciAdi = getKullaniciAdi();
        String islem = joinPoint.getSignature().getName();
        String detay = getMethodDetails(joinPoint);
        HttpServletRequest request = getRequest();

        try {
            Object result = joinPoint.proceed();
            auditLogService.logIslem(kullaniciAdi, islem, detay, "BAŞARILI", request);
            return result;
        } catch (Exception e) {
            String hataMesaji = String.format("BAŞARISIZ: %s - %s", e.getClass().getSimpleName(), e.getMessage());
            auditLogService.logIslem(kullaniciAdi, islem, detay, hataMesaji, request);
            throw e;
        }
    }

    private String getKullaniciAdi() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() ? 
               authentication.getName() : "ANONIM";
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new IllegalStateException("HTTP isteği bulunamadı");
        }
        return attributes.getRequest();
    }

    private String getMethodDetails(ProceedingJoinPoint joinPoint) {
        StringBuilder details = new StringBuilder();
        details.append("Metod: ").append(joinPoint.getSignature().getName());
        details.append(", Sınıf: ").append(joinPoint.getTarget().getClass().getSimpleName());
        details.append(", Parametreler: ").append(Arrays.toString(joinPoint.getArgs()));
        return details.toString();
    }
} 