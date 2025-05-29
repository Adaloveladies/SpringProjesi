package com.adaloveladies.SpringProjesi.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "gorevler", indexes = {
    @Index(name = "idx_gorev_kullanici", columnList = "kullanici_id"),
    @Index(name = "idx_gorev_durum", columnList = "durum"),
    @Index(name = "idx_gorev_son_tarih", columnList = "son_tarih"),
    @Index(name = "idx_gorev_tamamlanma", columnList = "tamamlanma_tarihi")
})
public class Gorev {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String baslik;

    @Column(length = 1000)
    private String aciklama;

    @Column(nullable = false)
    private Integer puanDegeri;

    @Column(name = "son_tarih", nullable = false)
    private LocalDateTime sonTarih;

    @Column(name = "tamamlanma_tarihi")
    private LocalDateTime tamamlanmaTarihi;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GorevDurumu durum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GorevTipi tip;

    @Enumerated(EnumType.STRING)
    private GorevTekrari tekrar;

    @Column
    private Boolean rutinOlustur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kullanici_id", nullable = false)
    private Kullanici kullanici;

    @Column(nullable = false)
    private LocalDateTime olusturmaTarihi;

    @PrePersist
    protected void onCreate() {
        olusturmaTarihi = LocalDateTime.now();
        if (durum == null) {
            durum = GorevDurumu.BEKLEMEDE;
        }
    }

    public enum GorevTipi {
        GUNLUK(10),    // 10 puan
        HAFTALIK(20),  // 20 puan
        AYLIK(30),      // 30 puan
        OZEL(30);      // 30 puan
        
        private final int puanDegeri;
        
        GorevTipi(int puanDegeri) {
            this.puanDegeri = puanDegeri;
        }
        
        public int getPuanDegeri() {
            return puanDegeri;
        }
    }

    public enum GorevTekrari {
        TEKRAR_YOK,
        GUNLUK,
        HAFTALIK,
        AYLIK
    }
} 