package com.adaloveladies.SpringProjesi.controller;

import com.adaloveladies.SpringProjesi.model.Building;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.service.BuildingService;
import com.adaloveladies.SpringProjesi.service.KullaniciService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buildings")
@RequiredArgsConstructor
public class BuildingController {
    private final BuildingService buildingService;
    private final KullaniciService kullaniciService;

    @PostMapping
    public ResponseEntity<Building> binaOlustur(
            @RequestParam Long kullaniciId,
            @RequestParam Long sehirId) {
        Building building = buildingService.binaOlustur(kullaniciId, sehirId);
        return ResponseEntity.ok(building);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Building> binaGuncelle(
            @PathVariable Long id,
            @RequestParam int katSayisi,
            @RequestParam boolean tamamlandi,
            @RequestParam boolean hasRoof) {
        Building building = buildingService.binaGuncelle(id, katSayisi, tamamlandi, hasRoof);
        return ResponseEntity.ok(building);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> binaSil(@PathVariable Long id) {
        buildingService.binaSil(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Building>> tumBinalariGetir() {
        List<Building> buildings = buildingService.tumBinalariGetir();
        return ResponseEntity.ok(buildings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Building> binaGetir(@PathVariable Long id) {
        return buildingService.binaGetir(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{kullaniciId}")
    public ResponseEntity<List<Building>> kullaniciBinalariniGetir(@PathVariable Long kullaniciId) {
        Kullanici kullanici = kullaniciService.kullaniciGetir(kullaniciId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        List<Building> buildings = buildingService.kullaniciBinalariniGetir(kullanici);
        return ResponseEntity.ok(buildings);
    }

    @PostMapping("/{id}/floor")
    public ResponseEntity<Building> katEkle(@PathVariable Long id) {
        Building building = buildingService.katEkle(id);
        return ResponseEntity.ok(building);
    }

    @PostMapping("/{id}/roof")
    public ResponseEntity<Building> catiEkle(@PathVariable Long id) {
        Building building = buildingService.catiEkle(id);
        return ResponseEntity.ok(building);
    }

    @PostMapping("/{id}/task")
    public ResponseEntity<Building> gorevTamamla(@PathVariable Long id) {
        Building building = buildingService.gorevTamamla(id);
        return ResponseEntity.ok(building);
    }
} 