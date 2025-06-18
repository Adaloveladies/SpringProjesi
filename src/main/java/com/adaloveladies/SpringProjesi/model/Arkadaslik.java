package com.adaloveladies.SpringProjesi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

/**
 * Arkadaşlık modeli
 * Kullanıcılar arasındaki arkadaşlık ilişkilerini temsil eder
 */
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "arkadasliklar", indexes = {
    @Index(name = "idx_arkadaslik_gonderen", columnList = "gonderen_id"),
    @Index(name = "idx_arkadaslik_alan", columnList = "alan_id"),
    @Index(name = "idx_arkadaslik_durum", columnList = "durum")
})
public class Arkadaslik {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gonderen_id", nullable = false)
    @JsonIgnore
    private Kullanici gonderen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alan_id", nullable = false)
    @JsonIgnore
    private Kullanici alan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ArkadaslikDurumu durum = ArkadaslikDurumu.BEKLEMEDE;

    @Column(name = "gonderme_tarihi", nullable = false)
    private LocalDateTime gondermeTarihi;

    @Column(name = "kabul_tarihi")
    private LocalDateTime kabulTarihi;

    @PrePersist
    protected void onCreate() {
        if (gondermeTarihi == null) {
            gondermeTarihi = LocalDateTime.now();
        }
        if (durum == null) {
            durum = ArkadaslikDurumu.BEKLEMEDE;
        }
    }

    public void kabulEt() {
        this.durum = ArkadaslikDurumu.KABUL_EDILDI;
        this.kabulTarihi = LocalDateTime.now();
    }

    public void reddet() {
        this.durum = ArkadaslikDurumu.REDDEDILDI;
    }

    public void iptalEt() {
        this.durum = ArkadaslikDurumu.IPTAL_EDILDI;
    }
} 