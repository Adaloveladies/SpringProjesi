package com.adaloveladies.SpringProjesi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JWT token yanıtı için DTO
 * Kullanıcı girişi sonrası dönen token ve kullanıcı bilgilerini içerir
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    @Builder.Default
    private String token = "";
    
    @Builder.Default
    private String tokenTipi = "Bearer";
    
    private Long kullaniciId;
    
    private String kullaniciAdi;
    
    private String email;
    
    private Integer seviye;
    
    private Integer puan;
    
    private String rol;
    
    private String profilResmi;
    
    private Integer tamamlananGorevSayisi;
    
    private Integer toplamGorevSayisi;
    
    /**
     * Başarı oranını hesaplar
     * @return Tamamlanan görev sayısının toplam görev sayısına oranı
     */
    public Double getBasariOrani() {
        if (toplamGorevSayisi == null || toplamGorevSayisi == 0) {
            return 0.0;
        }
        return (double) tamamlananGorevSayisi / toplamGorevSayisi * 100;
    }
} 