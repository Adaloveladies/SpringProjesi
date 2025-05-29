package com.adaloveladies.SpringProjesi.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByKullaniciAdiOrderByTarihDesc(String kullaniciAdi);
    List<AuditLog> findByTarihBetweenOrderByTarihDesc(LocalDateTime baslangic, LocalDateTime bitis);
    List<AuditLog> findByIslemOrderByTarihDesc(String islem);
} 