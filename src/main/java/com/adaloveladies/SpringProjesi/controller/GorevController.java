package com.adaloveladies.SpringProjesi.controller;

import com.adaloveladies.SpringProjesi.dto.GorevRequestDTO;
import com.adaloveladies.SpringProjesi.dto.GorevResponseDTO;
import com.adaloveladies.SpringProjesi.service.GorevService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gorevler")
@RequiredArgsConstructor
@Tag(name = "Görev Yönetimi", description = "Görev oluşturma, güncelleme, silme ve listeleme işlemleri")
@SecurityRequirement(name = "bearerAuth")
public class GorevController {
    private final GorevService gorevService;

    @PostMapping
    @Operation(
        summary = "Yeni görev oluştur",
        description = "Kullanıcı için yeni bir görev oluşturur"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Görev başarıyla oluşturuldu",
            content = @Content(schema = @Schema(implementation = GorevResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Geçersiz istek"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Yetkilendirme hatası"
        )
    })
    public ResponseEntity<GorevResponseDTO> gorevOlustur(
        @RequestBody GorevRequestDTO request,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(gorevService.gorevOlustur(request, userDetails.getUsername()));
    }

    @GetMapping
    @Operation(
        summary = "Görevleri listele",
        description = "Kullanıcının tüm görevlerini listeler"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Görevler başarıyla listelendi",
            content = @Content(schema = @Schema(implementation = GorevResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Yetkilendirme hatası"
        )
    })
    public ResponseEntity<List<GorevResponseDTO>> gorevleriListele(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(gorevService.gorevleriListele(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Görev detayı",
        description = "Belirli bir görevin detaylarını getirir"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Görev detayları başarıyla getirildi",
            content = @Content(schema = @Schema(implementation = GorevResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Görev bulunamadı"
        )
    })
    public ResponseEntity<GorevResponseDTO> gorevDetay(
        @PathVariable Long id,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(gorevService.gorevDetay(id, userDetails.getUsername()));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Görev güncelle",
        description = "Mevcut bir görevi günceller"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Görev başarıyla güncellendi",
            content = @Content(schema = @Schema(implementation = GorevResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Görev bulunamadı"
        )
    })
    public ResponseEntity<GorevResponseDTO> gorevGuncelle(
        @PathVariable Long id,
        @RequestBody GorevRequestDTO request,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(gorevService.gorevGuncelle(id, request, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Görev sil",
        description = "Belirli bir görevi siler"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Görev başarıyla silindi"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Görev bulunamadı"
        )
    })
    public ResponseEntity<Void> gorevSil(
        @PathVariable Long id,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        gorevService.gorevSil(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/tamamla")
    @Operation(
        summary = "Görevi tamamla",
        description = "Belirli bir görevi tamamlandı olarak işaretler"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Görev başarıyla tamamlandı",
            content = @Content(schema = @Schema(implementation = GorevResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Görev bulunamadı"
        )
    })
    public ResponseEntity<GorevResponseDTO> gorevTamamla(
        @PathVariable Long id,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(gorevService.gorevTamamla(id, userDetails.getUsername()));
    }
} 