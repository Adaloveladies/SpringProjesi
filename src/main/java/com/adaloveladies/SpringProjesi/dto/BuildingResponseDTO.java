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
public class BuildingResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Integer requiredLevel;
    private Integer floorCount;
    private Integer dailyCompletedTasks;
    private Boolean isCompleted;
    private Boolean hasRoof;
    private LocalDateTime completedAt;
    private Long userId;
    private String username;
} 