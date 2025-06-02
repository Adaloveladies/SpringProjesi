package com.adaloveladies.SpringProjesi.dto;

import com.adaloveladies.SpringProjesi.model.GorevTipi;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Görev oluşturma ve güncelleme istekleri için DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GorevRequestDTO {
    private String baslik;
    private String aciklama;
    private int puanDegeri;
    private GorevTipi gorevTipi;
    private LocalDateTime sonTarih;

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
} 