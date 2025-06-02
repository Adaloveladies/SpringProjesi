package com.adaloveladies.SpringProjesi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SehirResponseDTO {
    private Long id;
    private String ad;
    private Integer seviye;
    private Integer toplamPuan;
    private String gorunum;
    private Long kullaniciId;
    private String kullaniciAdi;
    private Boolean aktif;
    private Integer sonrakiSeviyePuani;
} 