package com.adaloveladies.SpringProjesi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogRequestDTO {
    @NotBlank(message = "Kullanıcı adı boş olamaz")
    private String kullaniciAdi;

    @NotBlank(message = "İşlem boş olamaz")
    private String islem;

    @NotBlank(message = "Detay boş olamaz")
    private String detay;

    @NotBlank(message = "Sonuç boş olamaz")
    private String sonuc;

    @NotBlank(message = "IP adresi boş olamaz")
    private String ipAdresi;

    @NotBlank(message = "User Agent boş olamaz")
    private String userAgent;
} 