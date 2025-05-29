package com.adaloveladies.SpringProjesi.controller;

import com.adaloveladies.SpringProjesi.model.Sehir;
import com.adaloveladies.SpringProjesi.service.SehirService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sehirler")
@RequiredArgsConstructor
@Tag(name = "Şehirler", description = "Şehir işlemleri için API endpoint'leri")
public class SehirController {

    private final SehirService sehirService;

    @GetMapping
    @Operation(summary = "Şehri getir", description = "Kullanıcının şehrini getirir")
    public ResponseEntity<Sehir> sehirGetir(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(sehirService.sehirGetir(Long.parseLong(userDetails.getUsername())));
    }

    @GetMapping("/seviye-atlayabilir")
    @Operation(summary = "Seviye atlayabilir mi", description = "Şehrin seviye atlayıp atlayamayacağını kontrol eder")
    public ResponseEntity<Boolean> seviyeAtlayabilirMi(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(sehirService.seviyeAtlayabilirMi(Long.parseLong(userDetails.getUsername())));
    }
} 