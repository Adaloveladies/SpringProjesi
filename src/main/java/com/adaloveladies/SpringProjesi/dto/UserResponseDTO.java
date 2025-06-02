package com.adaloveladies.SpringProjesi.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Set;

/**
 * Kullanıcı bilgilerini döndürmek için DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String ad;
    private String soyad;
    private Integer points;
    private Integer level;
    private Integer completedTaskCount;
    private Set<String> roller;
} 