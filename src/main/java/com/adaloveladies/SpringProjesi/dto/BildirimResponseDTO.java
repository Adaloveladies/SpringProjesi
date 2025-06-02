package com.adaloveladies.SpringProjesi.dto;

import com.adaloveladies.SpringProjesi.model.Bildirim;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BildirimResponseDTO {
    private Long id;
    private Long kullaniciId;
    private String kullaniciAdi;
    private String baslik;
    private String mesaj;
    private Bildirim.BildirimTipi tip;
    private Boolean okundu;
    private LocalDateTime olusturmaTarihi;
} 