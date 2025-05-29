package com.adaloveladies.SpringProjesi.dto;

import com.adaloveladies.SpringProjesi.model.Gorev;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GorevRequestDTO {
    private String baslik;
    private String aciklama;
    private LocalDateTime sonTarih;
    private Integer puan;
    private String kategori;
    private String tekrarTipi;
    private Gorev.GorevTipi tip;
    private Gorev.GorevTekrari tekrar;
    private Boolean rutinOlustur;
} 