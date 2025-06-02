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
public class BuildingRequestDTO {
    @NotBlank(message = "Bina adı boş olamaz")
    private String name;

    @NotBlank(message = "Bina açıklaması boş olamaz")
    private String description;

    @NotNull(message = "Gerekli seviye boş olamaz")
    @Positive(message = "Gerekli seviye pozitif olmalıdır")
    private Integer requiredLevel;

    @NotNull(message = "Kat sayısı boş olamaz")
    @Positive(message = "Kat sayısı pozitif olmalıdır")
    private Integer floorCount;
} 