package com.adaloveladies.SpringProjesi.dto;

import com.adaloveladies.SpringProjesi.model.GorevTipi;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * Görev oluşturma ve güncelleme istekleri için DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestDTO {
    private String baslik;
    private String aciklama;
    private GorevTipi gorevTipi;
    private Integer puanDegeri;
    private LocalDateTime bitisTarihi;
    private Long sehirId;

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

    public Integer getPuanDegeri() {
        return puanDegeri;
    }

    public void setPuanDegeri(Integer puanDegeri) {
        this.puanDegeri = puanDegeri;
    }

    public LocalDateTime getBitisTarihi() {
        return bitisTarihi;
    }

    public void setBitisTarihi(LocalDateTime bitisTarihi) {
        this.bitisTarihi = bitisTarihi;
    }

    public Long getSehirId() {
        return sehirId;
    }

    public void setSehirId(Long sehirId) {
        this.sehirId = sehirId;
    }
} 