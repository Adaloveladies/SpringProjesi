package com.adaloveladies.SpringProjesi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "binalar")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ad;

    @Column(length = 1000)
    private String aciklama;

    @Column(nullable = false)
    private Integer gerekliSeviye;

    @Column(nullable = false)
    private Integer katSayisi;

    @Column(nullable = false)
    private Integer gunlukTamamlananGorevSayisi;

    @Column(nullable = false)
    private boolean tamamlandi;

    @Column(nullable = false)
    private boolean hasRoof;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kullanici_id", nullable = false)
    private Kullanici kullanici;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sehir_id", nullable = false)
    private Sehir sehir;

    private LocalDateTime tamamlanmaTarihi;

    @PrePersist
    protected void onCreate() {
        if (gerekliSeviye == null) {
            gerekliSeviye = 1;
        }
        if (katSayisi == null) {
            katSayisi = 0;
        }
        if (gunlukTamamlananGorevSayisi == null) {
            gunlukTamamlananGorevSayisi = 0;
        }
        tamamlandi = false;
        hasRoof = false;
    }

    public void seviyeAtla() {
        gerekliSeviye++;
    }

    public boolean seviyeAtlayabilirMi() {
        return kullanici.getPoints() >= gerekliSeviye * 50;
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

    public Integer getGerekliSeviye() {
        return gerekliSeviye;
    }

    public void setGerekliSeviye(Integer gerekliSeviye) {
        this.gerekliSeviye = gerekliSeviye;
    }

    public Integer getKatSayisi() {
        return katSayisi;
    }

    public void setKatSayisi(Integer katSayisi) {
        this.katSayisi = katSayisi;
    }

    public Integer getGunlukTamamlananGorevSayisi() {
        return gunlukTamamlananGorevSayisi;
    }

    public void setGunlukTamamlananGorevSayisi(Integer gunlukTamamlananGorevSayisi) {
        this.gunlukTamamlananGorevSayisi = gunlukTamamlananGorevSayisi;
    }

    public Boolean getTamamlandi() {
        return tamamlandi;
    }

    public void setTamamlandi(Boolean tamamlandi) {
        this.tamamlandi = tamamlandi;
    }

    public boolean isHasRoof() {
        return hasRoof;
    }

    public void setHasRoof(Boolean hasRoof) {
        this.hasRoof = hasRoof;
    }

    public Kullanici getKullanici() {
        return kullanici;
    }

    public void setKullanici(Kullanici kullanici) {
        this.kullanici = kullanici;
    }

    public Sehir getSehir() {
        return sehir;
    }

    public void setSehir(Sehir sehir) {
        this.sehir = sehir;
    }

    public LocalDateTime getTamamlanmaTarihi() {
        return tamamlanmaTarihi;
    }

    public void setTamamlanmaTarihi(LocalDateTime tamamlanmaTarihi) {
        this.tamamlanmaTarihi = tamamlanmaTarihi;
    }
} 