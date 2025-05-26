package com.adaloveladies.SpringProjesi.dto;

import com.adaloveladies.SpringProjesi.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Görev oluşturma ve güncelleme istekleri için DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestDTO {

    @NotBlank(message = "Başlık boş olamaz")
    private String title;

    @NotBlank(message = "Açıklama boş olamaz")
    private String description;

    @NotNull(message = "Durum boş olamaz")
    private TaskStatus status;
} 