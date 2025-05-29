package com.adaloveladies.SpringProjesi.dto;

import com.adaloveladies.SpringProjesi.model.Gorev;
import com.adaloveladies.SpringProjesi.model.GorevDurumu;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class GorevResponseDTO {
    private Long id;
    private String baslik;
    private String aciklama;
    private Integer puanDegeri;
    private LocalDateTime sonTarih;
    private GorevDurumu durum;
    private Gorev.GorevTipi tip;
    private Gorev.GorevTekrari tekrar;
    private Boolean rutinOlustur;
    private Long kullaniciId;
    private String kullaniciAdi;
    private LocalDateTime olusturmaTarihi;
    private LocalDateTime tamamlanmaTarihi;
    private Integer puan;
    private String kategori;
    private String tekrarTipi;
    private boolean tamamlandi;
} 