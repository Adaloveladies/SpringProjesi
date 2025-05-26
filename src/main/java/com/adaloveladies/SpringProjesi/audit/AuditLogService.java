package com.adaloveladies.SpringProjesi.audit;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void logAction(String username, String action, String details, HttpServletRequest request) {
        AuditLog auditLog = AuditLog.builder()
                .username(username)
                .action(action)
                .details(details)
                .ipAddress(getClientIp(request))
                .timestamp(LocalDateTime.now())
                .status("SUCCESS")
                .build();

        auditLogRepository.save(auditLog);
    }

    public void logError(String username, String action, String details, String errorMessage, HttpServletRequest request) {
        AuditLog auditLog = AuditLog.builder()
                .username(username)
                .action(action)
                .details(details)
                .ipAddress(getClientIp(request))
                .timestamp(LocalDateTime.now())
                .status("ERROR")
                .errorMessage(errorMessage)
                .build();

        auditLogRepository.save(auditLog);
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0];
        }
        return request.getRemoteAddr();
    }
} 