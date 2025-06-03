package com.adaloveladies.SpringProjesi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "buildings")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kullanici_id", nullable = false)
    private Kullanici kullanici;

    @ManyToOne
    @JoinColumn(name = "sehir_id", nullable = false)
    private Sehir sehir;

    @Column(nullable = false)
    private int katSayisi = 0;

    @Column(nullable = false)
    private boolean tamamlandi = false;

    @Column
    private LocalDateTime tamamlanmaTarihi;

    @Column(nullable = false)
    private boolean hasRoof = false;

    @Column(nullable = false)
    private int gunlukTamamlananGorevSayisi = 0;

    @Column(nullable = false)
    private int gerekliSeviye = 1;

    @PrePersist
    protected void onCreate() {
        if (tamamlanmaTarihi == null) {
            tamamlanmaTarihi = LocalDateTime.now();
        }
    }

    public void seviyeAtla() {
        gerekliSeviye++;
    }

    public boolean seviyeAtlayabilirMi() {
        return kullanici.getPoints() >= gerekliSeviye * 50;
    }
} 