package com.adaloveladies.SpringProjesi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "rozetler")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rozet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kullanici_id", nullable = false)
    private Kullanici kullanici;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RozetTipi tip;
    
    @Column(nullable = false)
    private String ad;
    
    @Column(length = 500)
    private String aciklama;
    
    @Column(name = "kazanilma_tarihi", nullable = false)
    private LocalDateTime kazanilmaTarihi = LocalDateTime.now();
    
    public enum RozetTipi {
        PUAN,
        SEVIYE,
        GOREV_SAYISI,
        OZEL
    }
} 