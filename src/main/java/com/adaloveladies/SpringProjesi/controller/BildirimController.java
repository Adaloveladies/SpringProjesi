package com.adaloveladies.SpringProjesi.controller;

import com.adaloveladies.SpringProjesi.model.Bildirim;
import com.adaloveladies.SpringProjesi.service.BildirimService;
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

    @GetMapping
    public ResponseEntity<List<Bildirim>> getBildirimler(@AuthenticationPrincipal UserDetails userDetails) {
        // UserDetails'dan kullanıcı ID'sini al
        Long kullaniciId = 1L; // Geçici olarak sabit değer
        return ResponseEntity.ok(bildirimService.getKullaniciBildirimleri(kullaniciId));
    }

    @GetMapping("/okunmamis")
    public ResponseEntity<List<Bildirim>> getOkunmamisBildirimler(@AuthenticationPrincipal UserDetails userDetails) {
        Long kullaniciId = 1L; // Geçici olarak sabit değer
        return ResponseEntity.ok(bildirimService.getOkunmamisBildirimler(kullaniciId));
    }

    @GetMapping("/okunmamis/sayisi")
    public ResponseEntity<Long> getOkunmamisBildirimSayisi(@AuthenticationPrincipal UserDetails userDetails) {
        Long kullaniciId = 1L; // Geçici olarak sabit değer
        return ResponseEntity.ok(bildirimService.getOkunmamisBildirimSayisi(kullaniciId));
    }

    @PostMapping("/{id}/okundu")
    public ResponseEntity<Void> bildirimOkunduIsaretle(@PathVariable Long id) {
        bildirimService.bildirimOkunduIsaretle(id);
        return ResponseEntity.ok().build();
    }
} 