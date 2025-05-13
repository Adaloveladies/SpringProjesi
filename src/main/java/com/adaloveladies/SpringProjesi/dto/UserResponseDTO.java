package com.adaloveladies.SpringProjesi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Integer score; // Kullanıcının puanı
    private Integer level; // Kullanıcının seviyesi
} 