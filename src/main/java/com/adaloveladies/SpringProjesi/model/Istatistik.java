package com.adaloveladies.SpringProjesi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "istatistikler")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Istatistik {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "kullanici_id")
    @JsonIgnore
    private Kullanici kullanici;
    
    @Column(name = "tamamlanan_gorev_sayisi")
    private Integer tamamlananGorevSayisi;
    
    @Column(name = "toplam_puan")
    private Integer toplamPuan;
    
    @Column(name = "seviye")
    private Integer seviye;
    
    @Column(name = "basarim_sayisi")
    private Integer basarimSayisi;
    
    @Column(nullable = false)
    private Integer toplamGorevSayisi;
    
    @Column(nullable = false)
    private Integer gunlukGorevSayisi;
    
    @Column(nullable = false)
    private Integer haftalikGorevSayisi;
    
    @Column(nullable = false)
    private Integer aylikGorevSayisi;
    
    @Column(nullable = false)
    private Integer kazanilanRozetSayisi;
    
    @Column(nullable = false)
    private LocalDateTime tamamlanmaTarihi;
    
    @Column(nullable = false)
    private LocalDateTime kayitTarihi;
    
    public double getBasariOrani() {
        if (toplamGorevSayisi == 0) {
            return 0.0;
        }
        return (double) tamamlananGorevSayisi / toplamGorevSayisi * 100;
    }
    
    public void gorevEkle() {
        toplamGorevSayisi++;
        tamamlanmaTarihi = LocalDateTime.now();
    }
    
    public void gorevTamamla() {
        tamamlananGorevSayisi++;
        tamamlanmaTarihi = LocalDateTime.now();
    }
    
    public void haftalikGorevSayisiGuncelle(int sayi) {
        haftalikGorevSayisi = sayi;
        tamamlanmaTarihi = LocalDateTime.now();
    }
    
    public void aylikGorevSayisiGuncelle(int sayi) {
        aylikGorevSayisi = sayi;
        tamamlanmaTarihi = LocalDateTime.now();
    }
    
    public void puanEkle(int puan) {
        toplamPuan += puan;
        tamamlanmaTarihi = LocalDateTime.now();
    }
    
    public void rozetEkle() {
        kazanilanRozetSayisi++;
        tamamlanmaTarihi = LocalDateTime.now();
    }
    
    public void calismaSuresiEkle(int dakika) {
        // Implementation needed
    }
    
    public void gunlukGorevSayisiGuncelle(int sayi) {
        gunlukGorevSayisi = sayi;
        tamamlanmaTarihi = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Kullanici getKullanici() {
        return kullanici;
    }

    public void setKullanici(Kullanici kullanici) {
        this.kullanici = kullanici;
    }

    public Integer getToplamGorevSayisi() {
        return toplamGorevSayisi;
    }

    public void setToplamGorevSayisi(Integer toplamGorevSayisi) {
        this.toplamGorevSayisi = toplamGorevSayisi;
    }

    public Integer getTamamlananGorevSayisi() {
        return tamamlananGorevSayisi;
    }

    public void setTamamlananGorevSayisi(Integer tamamlananGorevSayisi) {
        this.tamamlananGorevSayisi = tamamlananGorevSayisi;
    }

    public Integer getGunlukGorevSayisi() {
        return gunlukGorevSayisi;
    }

    public void setGunlukGorevSayisi(Integer gunlukGorevSayisi) {
        this.gunlukGorevSayisi = gunlukGorevSayisi;
    }

    public Integer getHaftalikGorevSayisi() {
        return haftalikGorevSayisi;
    }

    public void setHaftalikGorevSayisi(Integer haftalikGorevSayisi) {
        this.haftalikGorevSayisi = haftalikGorevSayisi;
    }

    public Integer getAylikGorevSayisi() {
        return aylikGorevSayisi;
    }

    public void setAylikGorevSayisi(Integer aylikGorevSayisi) {
        this.aylikGorevSayisi = aylikGorevSayisi;
    }

    public Integer getToplamPuan() {
        return toplamPuan;
    }

    public void setToplamPuan(Integer toplamPuan) {
        this.toplamPuan = toplamPuan;
    }

    public Integer getKazanilanRozetSayisi() {
        return kazanilanRozetSayisi;
    }

    public void setKazanilanRozetSayisi(Integer kazanilanRozetSayisi) {
        this.kazanilanRozetSayisi = kazanilanRozetSayisi;
    }

    public LocalDateTime getTamamlanmaTarihi() {
        return tamamlanmaTarihi;
    }

    public void setTamamlanmaTarihi(LocalDateTime tamamlanmaTarihi) {
        this.tamamlanmaTarihi = tamamlanmaTarihi;
    }

    public LocalDateTime getKayitTarihi() {
        return kayitTarihi;
    }

    public void setKayitTarihi(LocalDateTime kayitTarihi) {
        this.kayitTarihi = kayitTarihi;
    }

    public Integer getSeviye() {
        return seviye;
    }

    public void setSeviye(Integer seviye) {
        this.seviye = seviye;
    }

    public Integer getBasarimSayisi() {
        return basarimSayisi;
    }

    public void setBasarimSayisi(Integer basarimSayisi) {
        this.basarimSayisi = basarimSayisi;
    }
} 