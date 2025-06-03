package com.adaloveladies.SpringProjesi.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import com.adaloveladies.SpringProjesi.model.GorevTipi;

@Data
@Builder
public class GorevResponseDTO {
    private Long id;
    private String baslik;
    private String aciklama;
    private Integer puanDegeri;
    private boolean tamamlandi;
    private Long kullaniciId;
    private String username;
    private LocalDateTime olusturmaTarihi;
    private LocalDateTime tamamlanmaTarihi;
    private LocalDateTime sonTarih;
    private GorevTipi gorevTipi;
} 