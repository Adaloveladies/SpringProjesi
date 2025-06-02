package com.adaloveladies.SpringProjesi.dto;

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
public class IstatistikRequestDTO {
    @NotNull(message = "Kullanıcı ID boş olamaz")
    private Long kullaniciId;

    @NotNull(message = "Toplam görev sayısı boş olamaz")
    @Positive(message = "Toplam görev sayısı pozitif olmalıdır")
    private Integer toplamGorevSayisi;

    @NotNull(message = "Tamamlanan görev sayısı boş olamaz")
    @Positive(message = "Tamamlanan görev sayısı pozitif olmalıdır")
    private Integer tamamlananGorevSayisi;

    @NotNull(message = "Günlük görev sayısı boş olamaz")
    @Positive(message = "Günlük görev sayısı pozitif olmalıdır")
    private Integer gunlukGorevSayisi;

    @NotNull(message = "Haftalık görev sayısı boş olamaz")
    @Positive(message = "Haftalık görev sayısı pozitif olmalıdır")
    private Integer haftalikGorevSayisi;

    @NotNull(message = "Aylık görev sayısı boş olamaz")
    @Positive(message = "Aylık görev sayısı pozitif olmalıdır")
    private Integer aylikGorevSayisi;

    @NotNull(message = "Toplam puan boş olamaz")
    @Positive(message = "Toplam puan pozitif olmalıdır")
    private Integer toplamPuan;

    @NotNull(message = "Kazanılan rozet sayısı boş olamaz")
    @Positive(message = "Kazanılan rozet sayısı pozitif olmalıdır")
    private Integer kazanilanRozetSayisi;
} 