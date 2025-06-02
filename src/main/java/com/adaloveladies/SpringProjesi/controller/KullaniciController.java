package com.adaloveladies.SpringProjesi.controller;

import com.adaloveladies.SpringProjesi.dto.KullaniciRequestDTO;
import com.adaloveladies.SpringProjesi.dto.KullaniciResponseDTO;
import com.adaloveladies.SpringProjesi.service.KullaniciService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kullanicilar")
@RequiredArgsConstructor
@Tag(name = "Kullanıcı Yönetimi", description = "Kullanıcı işlemleri için API endpoint'leri")
public class KullaniciController {

    private final KullaniciService kullaniciService;

    @PostMapping
    @Operation(
        summary = "Yeni kullanıcı oluştur",
        description = "Sistemde yeni bir kullanıcı hesabı oluşturur"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Kullanıcı başarıyla oluşturuldu",
            content = @Content(schema = @Schema(implementation = KullaniciResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Geçersiz istek"),
        @ApiResponse(responseCode = "409", description = "Kullanıcı adı veya email zaten kullanımda")
    })
    public ResponseEntity<KullaniciResponseDTO> kullaniciOlustur(
            @Parameter(description = "Kullanıcı bilgileri", required = true)
            @Valid @RequestBody KullaniciRequestDTO request) {
        return ResponseEntity.ok(kullaniciService.createKullanici(request));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Kullanıcı bilgilerini getir",
        description = "ID'ye göre kullanıcı bilgilerini getirir"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Kullanıcı bulundu",
            content = @Content(schema = @Schema(implementation = KullaniciResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı")
    })
    public ResponseEntity<KullaniciResponseDTO> kullaniciGetir(
            @Parameter(description = "Kullanıcı ID", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(kullaniciService.getKullaniciById(id));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Kullanıcı bilgilerini güncelle",
        description = "Mevcut kullanıcının bilgilerini günceller"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Kullanıcı başarıyla güncellendi",
            content = @Content(schema = @Schema(implementation = KullaniciResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Geçersiz istek"),
        @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı")
    })
    public ResponseEntity<KullaniciResponseDTO> kullaniciGuncelle(
            @Parameter(description = "Kullanıcı ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Güncellenecek kullanıcı bilgileri", required = true)
            @Valid @RequestBody KullaniciRequestDTO request) {
        return ResponseEntity.ok(kullaniciService.updateKullanici(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Kullanıcıyı sil",
        description = "Sistemden kullanıcıyı ve ilişkili tüm verileri siler"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Kullanıcı başarıyla silindi"),
        @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı")
    })
    public ResponseEntity<Void> kullaniciSil(
            @Parameter(description = "Kullanıcı ID", required = true)
            @PathVariable Long id) {
        kullaniciService.deleteKullanici(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(
        summary = "Tüm kullanıcıları listele",
        description = "Sistemdeki tüm kullanıcıları listeler"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Kullanıcılar başarıyla listelendi",
            content = @Content(schema = @Schema(implementation = KullaniciResponseDTO.class)))
    })
    public ResponseEntity<List<KullaniciResponseDTO>> tumKullanicilariGetir() {
        return ResponseEntity.ok(kullaniciService.getAllKullanicilar());
    }

    @PostMapping("/{id}/puan")
    @Operation(
        summary = "Kullanıcıya puan ekle",
        description = "Belirtilen kullanıcıya puan ekler"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Puan başarıyla eklendi",
            content = @Content(schema = @Schema(implementation = KullaniciResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Geçersiz puan değeri"),
        @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı")
    })
    public ResponseEntity<KullaniciResponseDTO> puanEkle(
            @Parameter(description = "Kullanıcı ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Eklenecek puan miktarı", required = true)
            @RequestParam Integer puan) {
        kullaniciService.addPuan(id, puan);
        return ResponseEntity.ok(kullaniciService.getKullaniciById(id));
    }
} 