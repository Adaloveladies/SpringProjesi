package com.adaloveladies.SpringProjesi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "istatistikler")
public class Istatistik {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "kullanici_id")
    private Kullanici kullanici;
    
    private Integer toplamGorevSayisi = 0;
    private Integer tamamlananGorevSayisi = 0;
    private Integer gunlukGorevSayisi = 0;
    private Integer haftalikGorevSayisi = 0;
    private Integer aylikGorevSayisi = 0;
    private Integer toplamPuan = 0;
    private Integer kazanilanRozetSayisi = 0;
    private Double basariOrani = 0.0;
    private Double ortalamaBasariOrani = 0.0;
    private Integer yuksekBasariOraniKullaniciSayisi = 0;
    private LocalDateTime sonGuncellemeTarihi;
    
    public Istatistik(Kullanici kullanici) {
        this.kullanici = kullanici;
        this.sonGuncellemeTarihi = LocalDateTime.now();
    }
    
    @PrePersist
    @PreUpdate
    public void onUpdate() {
        if (toplamGorevSayisi > 0) {
            basariOrani = (double) tamamlananGorevSayisi / toplamGorevSayisi * 100;
        }
        sonGuncellemeTarihi = LocalDateTime.now();
    }
} 