package com.adaloveladies.SpringProjesi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bildirimler", indexes = {
    @Index(name = "idx_bildirim_kullanici", columnList = "kullanici_id"),
    @Index(name = "idx_bildirim_okundu", columnList = "okundu"),
    @Index(name = "idx_bildirim_tarih", columnList = "olusturma_tarihi")
})
public class Bildirim {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String baslik;
    
    private String mesaj;
    
    @Column(name = "olusturma_tarihi")
    private LocalDateTime olusturmaTarihi;
    
    private Boolean okundu = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kullanici_id", nullable = false)
    private Kullanici kullanici;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "bildirim_tipi", nullable = false)
    private BildirimTipi bildirimTipi;
    
    @PrePersist
    protected void onCreate() {
        olusturmaTarihi = LocalDateTime.now();
    }
    
    public enum BildirimTipi {
        GOREV_TAMAMLANDI,
        SEVIYE_ATLANDI,
        PUAN_KAZANILDI,
        SISTEM_UYARI,
        ROZET_KAZANILDI
    }
} 