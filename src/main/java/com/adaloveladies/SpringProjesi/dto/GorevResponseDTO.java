package com.adaloveladies.SpringProjesi.dto;

import com.adaloveladies.SpringProjesi.model.GorevTipi;
import com.adaloveladies.SpringProjesi.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GorevResponseDTO {
    private Long id;
    private String baslik;
    private String aciklama;
    private int puanDegeri;
    private LocalDateTime sonTarih;
    private TaskStatus durum;
    private GorevTipi gorevTipi;
    private Long kullaniciId;
    private String username;
    private LocalDateTime olusturmaTarihi;
    private LocalDateTime tamamlanmaTarihi;

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

    public int getPuanDegeri() {
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

    public LocalDateTime getTamamlanmaTarihi() {
        return tamamlanmaTarihi;
    }

    public void setTamamlanmaTarihi(LocalDateTime tamamlanmaTarihi) {
        this.tamamlanmaTarihi = tamamlanmaTarihi;
    }

    public LocalDateTime getOlusturmaTarihi() {
        return olusturmaTarihi;
    }

    public void setOlusturmaTarihi(LocalDateTime olusturmaTarihi) {
        this.olusturmaTarihi = olusturmaTarihi;
    }

    public Long getKullaniciId() {
        return kullaniciId;
    }

    public void setKullaniciId(Long kullaniciId) {
        this.kullaniciId = kullaniciId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
} 