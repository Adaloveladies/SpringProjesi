package com.adaloveladies.SpringProjesi.controller;

import com.adaloveladies.SpringProjesi.audit.AuditLog;
import com.adaloveladies.SpringProjesi.audit.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/audit-logs")
@RequiredArgsConstructor
@Tag(name = "Denetim Günlüğü", description = "Denetim günlüğü işlemleri için API endpoint'leri")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping("/kullanici/{kullaniciAdi}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Kullanıcı loglarını getir", description = "Belirli bir kullanıcının denetim günlüğü kayıtlarını getirir")
    public ResponseEntity<List<AuditLog>> kullaniciLoglariniGetir(@PathVariable String kullaniciAdi) {
        return ResponseEntity.ok(auditLogService.kullaniciLoglariniGetir(kullaniciAdi));
    }

    @GetMapping("/tarih-araligi")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Tarih aralığı loglarını getir", description = "Belirli bir tarih aralığındaki denetim günlüğü kayıtlarını getirir")
    public ResponseEntity<List<AuditLog>> tarihAraligiLoglariniGetir(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime baslangic,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime bitis) {
        return ResponseEntity.ok(auditLogService.tarihAraligiLoglariniGetir(baslangic, bitis));
    }

    @GetMapping("/islem/{islem}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "İşlem loglarını getir", description = "Belirli bir işleme ait denetim günlüğü kayıtlarını getirir")
    public ResponseEntity<List<AuditLog>> islemLoglariniGetir(@PathVariable String islem) {
        return ResponseEntity.ok(auditLogService.islemLoglariniGetir(islem));
    }
} 