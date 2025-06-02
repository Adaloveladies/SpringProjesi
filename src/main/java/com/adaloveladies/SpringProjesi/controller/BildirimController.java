package com.adaloveladies.SpringProjesi.controller;

import com.adaloveladies.SpringProjesi.model.Bildirim;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.service.BildirimService;
import com.adaloveladies.SpringProjesi.service.KullaniciService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bildirimler")
@RequiredArgsConstructor
public class BildirimController {

    private final BildirimService bildirimService;
    private final KullaniciService kullaniciService;

    @GetMapping
    public ResponseEntity<List<Bildirim>> getBildirimler(@AuthenticationPrincipal UserDetails userDetails) {
        Kullanici kullanici = kullaniciService.findByKullaniciAdi(userDetails.getUsername());
        return ResponseEntity.ok(bildirimService.kullaniciBildirimleriniGetir(kullanici));
    }

    @GetMapping("/okunmamis")
    public ResponseEntity<List<Bildirim>> getOkunmamisBildirimler(@AuthenticationPrincipal UserDetails userDetails) {
        Kullanici kullanici = kullaniciService.findByKullaniciAdi(userDetails.getUsername());
        return ResponseEntity.ok(bildirimService.kullaniciBildirimleriniGetir(kullanici).stream()
                .filter(bildirim -> !bildirim.isOkundu())
                .toList());
    }

    @GetMapping("/okunmamis/sayisi")
    public ResponseEntity<Long> getOkunmamisBildirimSayisi(@AuthenticationPrincipal UserDetails userDetails) {
        Kullanici kullanici = kullaniciService.findByKullaniciAdi(userDetails.getUsername());
        return ResponseEntity.ok(bildirimService.kullaniciBildirimleriniGetir(kullanici).stream()
                .filter(bildirim -> !bildirim.isOkundu())
                .count());
    }

    @PostMapping("/{id}/okundu")
    public ResponseEntity<Void> bildirimOkunduIsaretle(@PathVariable Long id) {
        bildirimService.bildirimiOkunduOlarakIsaretle(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tumunu-okundu-isaretle")
    public ResponseEntity<Void> tumBildirimleriOkunduIsaretle(@AuthenticationPrincipal UserDetails userDetails) {
        Kullanici kullanici = kullaniciService.findByKullaniciAdi(userDetails.getUsername());
        bildirimService.tumBildirimleriOkunduOlarakIsaretle(kullanici);
        return ResponseEntity.ok().build();
    }
} 