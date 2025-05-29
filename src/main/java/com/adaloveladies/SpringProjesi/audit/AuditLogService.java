package com.adaloveladies.SpringProjesi.audit;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void logIslem(String kullaniciAdi, String islem, String detay, String sonuc, HttpServletRequest request) {
        AuditLog log = AuditLog.builder()
                .kullaniciAdi(kullaniciAdi)
                .islem(islem)
                .detay(detay)
                .sonuc(sonuc)
                .tarih(LocalDateTime.now())
                .ipAdresi(request.getRemoteAddr())
                .userAgent(request.getHeader("User-Agent"))
                .build();

        auditLogRepository.save(log);
    }

    public List<AuditLog> kullaniciLoglariniGetir(String kullaniciAdi) {
        return auditLogRepository.findByKullaniciAdiOrderByTarihDesc(kullaniciAdi);
    }

    public List<AuditLog> tarihAraligiLoglariniGetir(LocalDateTime baslangic, LocalDateTime bitis) {
        return auditLogRepository.findByTarihBetweenOrderByTarihDesc(baslangic, bitis);
    }

    public List<AuditLog> islemLoglariniGetir(String islem) {
        return auditLogRepository.findByIslemOrderByTarihDesc(islem);
    }
} 