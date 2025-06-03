package com.adaloveladies.SpringProjesi.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardDTO {
    private Long userId;
    private String username;
    private Integer points;
    private Integer rank;
    private Integer completedTaskCount;
} 