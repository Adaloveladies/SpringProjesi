package com.adaloveladies.SpringProjesi.dto;

import com.adaloveladies.SpringProjesi.model.Rozet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RozetResponseDTO {
    private Long id;
    private Long kullaniciId;
    private String kullaniciAdi;
    private String ad;
    private String aciklama;
    private Rozet.RozetTipi tip;
} 