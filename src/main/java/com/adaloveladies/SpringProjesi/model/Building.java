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
@Table(name = "buildings")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kullanici_id", nullable = false)
    @JsonIgnore
    private Kullanici kullanici;

    @ManyToOne
    @JoinColumn(name = "sehir_id", nullable = false)
    @JsonIgnore
    private Sehir sehir;

    @Column(nullable = false)
    @Builder.Default
    private int katSayisi = 0;

    @Column(nullable = false)
    @Builder.Default
    private boolean tamamlandi = false;

    @Column
    private LocalDateTime tamamlanmaTarihi;

    @Column(nullable = false)
    @Builder.Default
    private boolean hasRoof = false;

    @Column(nullable = false)
    @Builder.Default
    private int gunlukTamamlananGorevSayisi = 0;

    @Column(nullable = false)
    @Builder.Default
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