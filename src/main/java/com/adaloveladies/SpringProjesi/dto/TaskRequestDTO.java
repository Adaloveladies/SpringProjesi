package com.adaloveladies.SpringProjesi.dto;

import com.adaloveladies.SpringProjesi.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Görev başlığı boş olamaz")
    @Size(min = 3, max = 100, message = "Görev başlığı 3-100 karakter arasında olmalıdır")
    private String title;

    @Size(max = 1000, message = "Görev açıklaması en fazla 1000 karakter olabilir")
    private String description;

    private TaskStatus status;
} 