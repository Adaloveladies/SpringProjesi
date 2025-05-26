package com.adaloveladies.SpringProjesi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardDTO {
    private Long id;
    private String username;
    private Integer score;
    private Integer level;
    private Integer completedBuildings;
} 