package com.adaloveladies.SpringProjesi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "rozetler", indexes = {
    @Index(name = "idx_rozet_kullanici", columnList = "kullanici_id"),
    @Index(name = "idx_rozet_tip", columnList = "tip"),
    @Index(name = "idx_rozet_kazanilma_tarihi", columnList = "kazanilma_tarihi")
})
public class Rozet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kullanici_id", nullable = false)
    @JsonIgnore
    private Kullanici kullanici;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RozetTipi tip;

    @Column(nullable = false)
    private String ad;

    @Column(length = 1000)
    private String aciklama;

    @Column(nullable = false)
    private String ikon;

    @Column(nullable = false)
    private int puanDegeri;

    @Column(nullable = false)
    private LocalDateTime kazanilmaTarihi;

    @PrePersist
    protected void onCreate() {
        kazanilmaTarihi = LocalDateTime.now();
    }

    public enum RozetTipi {
        PUAN,
        SEVIYE,
        GOREV_SAYISI,
        GOREV_USTASI,
        GOREV_EFENDISI,
        OZEL
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public LocalDateTime getKazanilmaTarihi() {
        return kazanilmaTarihi;
    }

    public void setKazanilmaTarihi(LocalDateTime kazanilmaTarihi) {
        this.kazanilmaTarihi = kazanilmaTarihi;
    }

    public Kullanici getKullanici() {
        return kullanici;
    }

    public void setKullanici(Kullanici kullanici) {
        this.kullanici = kullanici;
    }

    public String getIkon() {
        return ikon;
    }

    public void setIkon(String ikon) {
        this.ikon = ikon;
    }

    public int getPuanDegeri() {
        return puanDegeri;
    }

    public void setPuanDegeri(int puanDegeri) {
        this.puanDegeri = puanDegeri;
    }

    public RozetTipi getTip() {
        return tip;
    }

    public void setTip(RozetTipi tip) {
        this.tip = tip;
    }
} 