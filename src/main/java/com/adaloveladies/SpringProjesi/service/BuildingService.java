package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.model.Building;
import com.adaloveladies.SpringProjesi.model.User;
import com.adaloveladies.SpringProjesi.repository.BuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuildingService {

    private final BuildingRepository buildingRepository;

    /**
     * Kullanıcının tüm binalarını getirir
     */
    public List<Building> getAllBuildings(User user) {
        return buildingRepository.findByUserOrderByRequiredLevelAsc(user);
    }

    /**
     * Kullanıcının tamamlanmamış binalarını getirir
     */
    public List<Building> getIncompleteBuildings(User user) {
        return buildingRepository.findByUserAndIsCompletedFalseOrderByRequiredLevelAsc(user);
    }

    /**
     * Kullanıcının bir sonraki inşa edilecek binasını getirir
     */
    public Building getNextBuilding(User user) {
        return buildingRepository.findFirstByUserAndIsCompletedFalseOrderByRequiredLevelAsc(user);
    }

    /**
     * Yeni bina oluşturur
     */
    @Transactional
    public Building createBuilding(Building building, User user) {
        building.setUser(user);
        building.setIsCompleted(false);
        return buildingRepository.save(building);
    }

    /**
     * Bina durumunu günceller
     */
    @Transactional
    public Building updateBuildingStatus(Long buildingId, boolean isCompleted, User user) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new RuntimeException("Bina bulunamadı"));

        if (!building.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bu binayı güncelleme yetkiniz yok");
        }

        building.setIsCompleted(isCompleted);
        return buildingRepository.save(building);
    }
} 