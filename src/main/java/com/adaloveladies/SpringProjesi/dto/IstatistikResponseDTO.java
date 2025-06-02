package com.adaloveladies.SpringProjesi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IstatistikResponseDTO {
    private Long id;
    private Long kullaniciId;
    private String kullaniciAdi;
    private Integer toplamGorevSayisi;
    private Integer tamamlananGorevSayisi;
    private Integer gunlukGorevSayisi;
    private Integer haftalikGorevSayisi;
    private Integer aylikGorevSayisi;
    private Integer toplamPuan;
    private Integer kazanilanRozetSayisi;
    private Double basariOrani;
    private Double ortalamaBasariOrani;
    private Integer yuksekBasariOraniKullaniciSayisi;
    private LocalDateTime sonGuncellemeTarihi;
} 