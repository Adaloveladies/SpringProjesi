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
public class AuditLogResponseDTO {
    private Long id;
    private String kullaniciAdi;
    private String islem;
    private String detay;
    private String sonuc;
    private LocalDateTime tarih;
    private String ipAdresi;
    private String userAgent;
} 