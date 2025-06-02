package com.adaloveladies.SpringProjesi.dto;

import com.adaloveladies.SpringProjesi.model.Rozet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RozetRequestDTO {
    @NotNull(message = "Kullanıcı ID boş olamaz")
    private Long kullaniciId;

    @NotBlank(message = "Rozet adı boş olamaz")
    private String ad;

    @NotBlank(message = "Rozet açıklaması boş olamaz")
    private String aciklama;

    @NotNull(message = "Rozet tipi boş olamaz")
    private Rozet.RozetTipi tip;
} 