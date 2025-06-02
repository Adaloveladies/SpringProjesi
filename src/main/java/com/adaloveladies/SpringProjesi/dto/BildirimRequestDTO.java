package com.adaloveladies.SpringProjesi.dto;

import com.adaloveladies.SpringProjesi.model.Bildirim;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BildirimRequestDTO {
    @NotNull(message = "Kullanıcı ID boş olamaz")
    private Long kullaniciId;

    @NotBlank(message = "Bildirim başlığı boş olamaz")
    private String baslik;

    @NotBlank(message = "Bildirim mesajı boş olamaz")
    private String mesaj;

    @NotNull(message = "Bildirim tipi boş olamaz")
    private Bildirim.BildirimTipi tip;
} 