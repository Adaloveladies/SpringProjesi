package com.adaloveladies.SpringProjesi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sehirler")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sehir {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ad;

    @Column(nullable = false)
    private Integer seviye;

    @Column(nullable = false)
    private Integer toplamPuan;

    @Column(nullable = false)
    private String gorunum;

    @OneToOne
    @JoinColumn(name = "kullanici_id", nullable = false)
    private Kullanici kullanici;

    @Column(nullable = false)
    private Boolean aktif;

    @Column(nullable = false)
    private Integer sonrakiSeviyePuani;

    public void seviyeAtla() {
        this.seviye++;
        this.sonrakiSeviyePuani = this.seviye * 1000;
        this.gorunum = "sehir_" + this.seviye + ".png";
    }

    public boolean seviyeAtlayabilirMi() {
        return this.toplamPuan >= this.sonrakiSeviyePuani;
    }
} 