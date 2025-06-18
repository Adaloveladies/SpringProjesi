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
@Table(name = "gorevler")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @JsonIgnore
    private Kullanici kullanici;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sehir_id")
    @JsonIgnore
    private Sehir sehir;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    @JsonIgnore
    private Building building;

    @Column(nullable = false)
    @Builder.Default
    private boolean tamamlandi = false;

    @Column(name = "olusturma_tarihi")
    @Builder.Default
    private LocalDateTime olusturmaTarihi = LocalDateTime.now();

    @Column(name = "tamamlanma_tarihi")
    private LocalDateTime tamamlanmaTarihi;

    @Column(name = "son_tarih")
    private LocalDateTime sonTarih;

    @Builder.Default
    private Integer puanDegeri = 10;

    @Column(nullable = false)
    @Builder.Default
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