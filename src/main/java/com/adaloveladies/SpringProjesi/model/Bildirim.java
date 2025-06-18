package com.adaloveladies.SpringProjesi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "bildirimler", indexes = {
    @Index(name = "idx_bildirim_kullanici", columnList = "kullanici_id"),
    @Index(name = "idx_bildirim_okundu", columnList = "okundu"),
    @Index(name = "idx_bildirim_tip", columnList = "bildirim_tipi"),
    @Index(name = "idx_bildirim_olusturma_tarihi", columnList = "olusturma_tarihi")
})
public class Bildirim {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kullanici_id", nullable = false)
    @JsonIgnore
    private Kullanici kullanici;
    
    @Column(nullable = false, length = 100)
    private String baslik;
    
    @Column(nullable = false, length = 500)
    private String mesaj;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "bildirim_tipi", nullable = false)
    private BildirimTipi tip;
    
    @Column(nullable = false)
    private boolean okundu;
    
    @Column(name = "olusturma_tarihi", nullable = false)
    private LocalDateTime olusturmaTarihi;
    
    @PrePersist
    protected void onCreate() {
        olusturmaTarihi = LocalDateTime.now();
        okundu = false;
    }
    
    public enum BildirimTipi {
        GOREV,
        SEVIYE,
        ROZET,
        SISTEM,
        GENEL,
        SEVIYE_ATLAMA,
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public boolean isOkundu() {
        return okundu;
    }

    public void setOkundu(boolean okundu) {
        this.okundu = okundu;
    }

    public LocalDateTime getOlusturmaTarihi() {
        return olusturmaTarihi;
    }

    public void setOlusturmaTarihi(LocalDateTime olusturmaTarihi) {
        this.olusturmaTarihi = olusturmaTarihi;
    }

    public Kullanici getKullanici() {
        return kullanici;
    }

    public void setKullanici(Kullanici kullanici) {
        this.kullanici = kullanici;
    }

    public void okunduOlarakIsaretle() {
        okundu = true;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public BildirimTipi getTip() {
        return tip;
    }

    public void setTip(BildirimTipi tip) {
        this.tip = tip;
    }
} 