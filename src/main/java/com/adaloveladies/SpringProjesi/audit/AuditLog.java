package com.adaloveladies.SpringProjesi.audit;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String kullaniciAdi;

    @Column(nullable = false)
    private String islem;

    @Column(length = 1000)
    private String detay;

    @Column(nullable = false)
    private String sonuc;

    @Column(nullable = false)
    private LocalDateTime tarih;

    @Column
    private String ipAdresi;

    @Column
    private String userAgent;
} 