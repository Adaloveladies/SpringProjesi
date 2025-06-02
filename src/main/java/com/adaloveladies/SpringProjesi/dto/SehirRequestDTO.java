package com.adaloveladies.SpringProjesi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SehirRequestDTO {
    @NotBlank(message = "Şehir adı boş olamaz")
    private String ad;

    @NotNull(message = "Şehir seviyesi boş olamaz")
    @Positive(message = "Şehir seviyesi pozitif olmalıdır")
    private Integer seviye;

    @NotNull(message = "Toplam puan boş olamaz")
    @Positive(message = "Toplam puan pozitif olmalıdır")
    private Integer toplamPuan;

    @NotBlank(message = "Şehir görünümü boş olamaz")
    private String gorunum;
} 