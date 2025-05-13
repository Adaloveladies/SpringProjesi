package com.adaloveladies.SpringProjesi.controller;

import com.adaloveladies.SpringProjesi.model.Building;
import com.adaloveladies.SpringProjesi.model.User;
import com.adaloveladies.SpringProjesi.service.BuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buildings")
@RequiredArgsConstructor
public class BuildingController {

    private final BuildingService buildingService;

    /**
     * Kullanıcının tüm binalarını getirir
     */
    @GetMapping
    public ResponseEntity<List<Building>> getAllBuildings(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(buildingService.getAllBuildings(user));
    }

    /**
     * Kullanıcının tamamlanmamış binalarını getirir
     */
    @GetMapping("/incomplete")
    public ResponseEntity<List<Building>> getIncompleteBuildings(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(buildingService.getIncompleteBuildings(user));
    }

    /**
     * Kullanıcının bir sonraki inşa edilecek binasını getirir
     */
    @GetMapping("/next")
    public ResponseEntity<Building> getNextBuilding(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(buildingService.getNextBuilding(user));
    }

    /**
     * Yeni bina oluşturur
     */
    @PostMapping
    public ResponseEntity<Building> createBuilding(
            @RequestBody Building building,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(buildingService.createBuilding(building, user));
    }

    /**
     * Bina durumunu günceller
     */
    @PatchMapping("/{buildingId}/status")
    public ResponseEntity<Building> updateBuildingStatus(
            @PathVariable Long buildingId,
            @RequestParam boolean isCompleted,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(buildingService.updateBuildingStatus(buildingId, isCompleted, user));
    }
} 