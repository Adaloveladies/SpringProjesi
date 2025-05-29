package com.adaloveladies.SpringProjesi.controller;

import com.adaloveladies.SpringProjesi.model.Istatistik;
import com.adaloveladies.SpringProjesi.service.IstatistikService;
import com.adaloveladies.SpringProjesi.service.KullaniciService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/istatistikler")
@RequiredArgsConstructor
public class IstatistikController {
    private final IstatistikService istatistikService;
    private final KullaniciService kullaniciService;

    @GetMapping("/genel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getGenelIstatistikler() {
        return ResponseEntity.ok(istatistikService.getGenelIstatistikler());
    }

    @GetMapping("/kullanici/{kullaniciId}")
    @PreAuthorize("hasRole('ADMIN') or #kullaniciId == authentication.principal.id")
    public ResponseEntity<Istatistik> getKullaniciIstatistikleri(@PathVariable Long kullaniciId) {
        var kullanici = kullaniciService.findById(kullaniciId);
        return ResponseEntity.ok(istatistikService.getKullaniciIstatistikleri(kullanici));
    }

    @PostMapping("/guncelle/{kullaniciId}")
    @PreAuthorize("hasRole('ADMIN') or #kullaniciId == authentication.principal.id")
    public ResponseEntity<Istatistik> istatistikleriGuncelle(@PathVariable Long kullaniciId) {
        var kullanici = kullaniciService.findById(kullaniciId);
        istatistikService.istatistikleriGuncelle(kullanici);
        return ResponseEntity.ok(istatistikService.getKullaniciIstatistikleri(kullanici));
    }
} 