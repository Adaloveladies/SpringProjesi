package com.adaloveladies.SpringProjesi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KullaniciResponseDTO {
    private Long id;
    private String username;
    private String email;
    private Integer points;
    private Integer level;
    private Integer completedTaskCount;
    private LocalDateTime creationDate;
    private boolean active;
    private Set<String> roles;
} 