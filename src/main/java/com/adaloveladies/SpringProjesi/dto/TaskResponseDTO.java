package com.adaloveladies.SpringProjesi.dto;

import com.adaloveladies.SpringProjesi.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Görev detaylarını döndürmek için DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private Long userId;
    private String username; // Kullanıcı adı
} 