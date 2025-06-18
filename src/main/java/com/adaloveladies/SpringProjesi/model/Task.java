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

/**
 * Görev modeli
 * Kullanıcıların oluşturduğu görevleri temsil eder
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "gorevler")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String baslik;

    @Column(length = 1000)
    private String aciklama;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GorevTipi gorevTipi;

    @Column(nullable = false)
    private Integer puanDegeri;

    @Column(name = "son_tarih")
    private LocalDateTime sonTarih;

    @Column(name = "tamamlandi")
    private boolean tamamlandi;

    @Column(name = "tamamlanma_tarihi")
    private LocalDateTime tamamlanmaTarihi;

    @Column(name = "olusturma_tarihi")
    private LocalDateTime olusturmaTarihi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kullanici_id", nullable = false)
    @JsonIgnore
    private Kullanici kullanici;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus durum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sehir_id")
    @JsonIgnore
    private Sehir sehir;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    @JsonIgnore
    private Building building;

    @PrePersist
    protected void onCreate() {
        olusturmaTarihi = LocalDateTime.now();
        if (durum == null) {
            durum = TaskStatus.BEKLEMEDE;
        }
        if (puanDegeri == null) {
            puanDegeri = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if (durum == TaskStatus.TAMAMLANDI && tamamlanmaTarihi == null) {
            tamamlanmaTarihi = LocalDateTime.now();
        }
    }

    public void tamamla() {
        durum = TaskStatus.TAMAMLANDI;
        tamamlanmaTarihi = LocalDateTime.now();
        kullanici.completeTask();
    }

    public void iptalEt() {
        durum = TaskStatus.IPTAL_EDILDI;
    }

    public void baslat() {
        durum = TaskStatus.DEVAM_EDIYOR;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public Integer getPuanDegeri() {
        return puanDegeri;
    }

    public void setPuanDegeri(Integer puanDegeri) {
        this.puanDegeri = puanDegeri;
    }

    public TaskStatus getDurum() {
        return durum;
    }

    public void setDurum(TaskStatus durum) {
        this.durum = durum;
    }

    public GorevTipi getGorevTipi() {
        return gorevTipi;
    }

    public void setGorevTipi(GorevTipi gorevTipi) {
        this.gorevTipi = gorevTipi;
    }

    public LocalDateTime getSonTarih() {
        return sonTarih;
    }

    public void setSonTarih(LocalDateTime sonTarih) {
        this.sonTarih = sonTarih;
    }

    public boolean isTamamlandi() {
        return tamamlandi;
    }

    public void setTamamlandi(boolean tamamlandi) {
        this.tamamlandi = tamamlandi;
    }

    public LocalDateTime getOlusturmaTarihi() {
        return olusturmaTarihi;
    }

    public void setOlusturmaTarihi(LocalDateTime olusturmaTarihi) {
        this.olusturmaTarihi = olusturmaTarihi;
    }

    public LocalDateTime getTamamlanmaTarihi() {
        return tamamlanmaTarihi;
    }

    public void setTamamlanmaTarihi(LocalDateTime tamamlanmaTarihi) {
        this.tamamlanmaTarihi = tamamlanmaTarihi;
    }

    public Kullanici getKullanici() {
        return kullanici;
    }

    public void setKullanici(Kullanici kullanici) {
        this.kullanici = kullanici;
    }

    public Sehir getSehir() {
        return sehir;
    }

    public void setSehir(Sehir sehir) {
        this.sehir = sehir;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
} 