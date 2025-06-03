package com.adaloveladies.SpringProjesi.dto;

import lombok.Data;
import java.time.LocalDateTime;
import com.adaloveladies.SpringProjesi.model.GorevTipi;

/**
 * Görev oluşturma ve güncelleme istekleri için DTO
 */
@Data
public class GorevRequestDTO {
    private String baslik;
    private String aciklama;
    private Integer puanDegeri;
    private LocalDateTime sonTarih;
    private GorevTipi gorevTipi;
    private String durum;

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

    public LocalDateTime getSonTarih() {
        return sonTarih;
    }

    public void setSonTarih(LocalDateTime sonTarih) {
        this.sonTarih = sonTarih;
    }

    public GorevTipi getGorevTipi() {
        return gorevTipi;
    }

    public void setGorevTipi(GorevTipi gorevTipi) {
        this.gorevTipi = gorevTipi;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }
} 