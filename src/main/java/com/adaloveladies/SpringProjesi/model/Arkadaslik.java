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
@Table(name = "arkadasliklar")
public class Arkadaslik {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "gonderen_id")
    private Kullanici gonderen;

    @ManyToOne
    @JoinColumn(name = "alan_id")
    private Kullanici alan;

    @Enumerated(EnumType.STRING)
    private ArkadaslikDurumu durum = ArkadaslikDurumu.BEKLEMEDE;

    private LocalDateTime gondermeTarihi;
    private LocalDateTime kabulTarihi;

    @PrePersist
    protected void onCreate() {
        gondermeTarihi = LocalDateTime.now();
    }
} 