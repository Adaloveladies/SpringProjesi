package com.adaloveladies.SpringProjesi.dto;

import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Builder
public class KullaniciResponseDTO {
    private Long id;
    private String kullaniciAdi;
    private String email;
    private Integer puan;
    private Integer seviye;
    private LocalDateTime olusturmaTarihi;
} 