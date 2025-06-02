package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.model.Building;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.repository.BuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuildingService {
    private final BuildingRepository buildingRepository;

    @Transactional
    public Building binaOlustur(Kullanici kullanici, String ad, String aciklama) {
        Building building = Building.builder()
                .ad(ad)
                .aciklama(aciklama)
                .kullanici(kullanici)
                .build();
        return buildingRepository.save(building);
    }

    @Transactional
    public Building binaGuncelle(Long id, String ad, String aciklama) {
        Building building = buildingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bina bulunamadı"));
        building.setAd(ad);
        building.setAciklama(aciklama);
        return buildingRepository.save(building);
    }

    @Transactional
    public void binaSil(Long id) {
        buildingRepository.deleteById(id);
    }

    public List<Building> tumBinalariGetir() {
        return buildingRepository.findAll();
    }

    public Optional<Building> binaGetir(Long id) {
        return buildingRepository.findById(id);
    }

    public List<Building> kullaniciBinalariniGetir(Kullanici kullanici) {
        return buildingRepository.findByKullaniciAndTamamlandiOrderByGerekliSeviyeAsc(kullanici, false);
    }

    @Transactional
    public Building katEkle(Long id) {
        Building building = buildingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bina bulunamadı"));
        building.setKatSayisi(building.getKatSayisi() + 1);
        return buildingRepository.save(building);
    }

    @Transactional
    public Building catiEkle(Long id) {
        Building building = buildingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bina bulunamadı"));
        if (!building.isHasRoof()) {
            building.setHasRoof(true);
            building.setTamamlandi(true);
            building.setTamamlanmaTarihi(LocalDateTime.now());
            return buildingRepository.save(building);
        }
        throw new RuntimeException("Bina zaten çatıya sahip");
    }

    @Transactional
    public Building gorevTamamla(Long id) {
        Building building = buildingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bina bulunamadı"));
        building.setGunlukTamamlananGorevSayisi(building.getGunlukTamamlananGorevSayisi() + 1);
        return buildingRepository.save(building);
    }
} 