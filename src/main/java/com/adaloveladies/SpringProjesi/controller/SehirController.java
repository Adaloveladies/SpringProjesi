package com.adaloveladies.SpringProjesi.controller;

import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.model.Sehir;
import com.adaloveladies.SpringProjesi.service.KullaniciService;
import com.adaloveladies.SpringProjesi.service.SehirService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sehirler")
@RequiredArgsConstructor
@Tag(name = "Şehirler", description = "Şehir işlemleri için API endpoint'leri")
public class SehirController {

    private final SehirService sehirService;
    private final KullaniciService kullaniciService;

    @PostMapping
    @Operation(summary = "Şehir oluştur", description = "Yeni bir şehir oluşturur")
    public ResponseEntity<Sehir> sehirOlustur(@RequestParam Long kullaniciId) {
        Kullanici kullanici = kullaniciService.kullaniciGetir(kullaniciId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        return ResponseEntity.ok(sehirService.sehirOlustur(kullanici));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Şehir güncelle", description = "Mevcut bir şehri günceller")
    public ResponseEntity<Sehir> sehirGuncelle(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String description) {
        return ResponseEntity.ok(sehirService.sehirGuncelle(id, name, description));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Şehir sil", description = "Bir şehri siler")
    public ResponseEntity<Void> sehirSil(@PathVariable Long id) {
        sehirService.sehirSil(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Tüm şehirleri getir", description = "Tüm şehirleri getirir")
    public ResponseEntity<List<Sehir>> tumSehirleriGetir() {
        return ResponseEntity.ok(sehirService.tumSehirleriGetir());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Şehir getir", description = "ID'ye göre şehir bilgilerini getirir")
    public ResponseEntity<Sehir> sehirGetir(@PathVariable Long id) {
        return sehirService.sehirGetir(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/kullanici/{kullaniciId}")
    @Operation(summary = "Kullanıcının şehirini getir", description = "Kullanıcının şehirini getirir")
    public ResponseEntity<Sehir> kullaniciSehriGetir(@PathVariable Long kullaniciId) {
        Kullanici kullanici = kullaniciService.kullaniciGetir(kullaniciId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        return sehirService.kullaniciSehriGetir(kullanici)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/puan")
    @Operation(summary = "Puan ekle", description = "Şehre puan ekler")
    public ResponseEntity<Void> puanEkle(
            @PathVariable Long id,
            @RequestParam int points) {
        Kullanici kullanici = kullaniciService.kullaniciGetir(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        sehirService.puanEkle(kullanici, points);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/seviye")
    @Operation(summary = "Seviye atla", description = "Şehrin seviyesini artırır")
    public ResponseEntity<Void> seviyeAtla(@PathVariable Long id) {
        sehirService.seviyeAtla(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/seviye-kontrol")
    @Operation(summary = "Seviye atlayabilir mi?", description = "Şehrin seviyesini atlayabilir mi?")
    public ResponseEntity<Boolean> seviyeAtlayabilirMi(@PathVariable Long id) {
        return ResponseEntity.ok(sehirService.seviyeAtlayabilirMi(id));
    }
} 