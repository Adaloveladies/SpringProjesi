package com.adaloveladies.SpringProjesi.controller;

import com.adaloveladies.SpringProjesi.dto.AuthResponseDTO;
import com.adaloveladies.SpringProjesi.dto.AuthRequestDTO;
import com.adaloveladies.SpringProjesi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Kimlik Doğrulama", description = "Kimlik doğrulama ve yetkilendirme işlemleri")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
        summary = "Yeni kullanıcı kaydı",
        description = "Sistemde yeni bir kullanıcı hesabı oluşturur"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Kayıt başarılı",
            content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Geçersiz istek"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Kullanıcı adı veya email zaten kullanımda"
        )
    })
    public ResponseEntity<AuthResponseDTO> register(@RequestBody AuthRequestDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(
        summary = "Kullanıcı girişi",
        description = "Mevcut kullanıcı hesabı ile giriş yapar"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Giriş başarılı",
            content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Geçersiz kullanıcı adı veya şifre"
        )
    })
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
