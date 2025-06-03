package com.adaloveladies.SpringProjesi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "gorevler")
public class Gorev {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String baslik;

    @Column(length = 1000)
    private String aciklama;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kullanici_id", nullable = false)
    private Kullanici kullanici;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sehir_id")
    private Sehir sehir;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building building;

    @Column(nullable = false)
    private boolean tamamlandi = false;

    @Column(name = "olusturma_tarihi")
    private LocalDateTime olusturmaTarihi = LocalDateTime.now();

    @Column(name = "tamamlanma_tarihi")
    private LocalDateTime tamamlanmaTarihi;

    @Column(name = "son_tarih")
    private LocalDateTime sonTarih;

    private Integer puanDegeri = 10;

    @Column(nullable = false)
    private String durum = "BEKLEMEDE";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GorevTipi gorevTipi;

    @PrePersist
    protected void onCreate() {
        olusturmaTarihi = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        if (tamamlandi && tamamlanmaTarihi == null) {
            tamamlanmaTarihi = LocalDateTime.now();
        }
    }
} 