package com.adaloveladies.SpringProjesi.dto;

import lombok.*;

/**
 * JWT yanıt veri transfer nesnesi
 * Kimlik doğrulama sonrası dönen token ve kullanıcı bilgilerini içerir
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponseDTO {
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
     * Kullanıcının başarı oranını hesaplar
     * @return Tamamlanan görevlerin toplam görevlere oranı (0-100 arası)
     */
    public double getBasariOrani() {
        if (toplamGorevSayisi == 0) return 0;
        return (double) tamamlananGorevSayisi / toplamGorevSayisi * 100;
    }
} 