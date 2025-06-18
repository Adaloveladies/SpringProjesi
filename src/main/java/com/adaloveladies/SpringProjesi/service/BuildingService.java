package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.model.Building;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.model.Sehir;
import com.adaloveladies.SpringProjesi.repository.BuildingRepository;
import com.adaloveladies.SpringProjesi.repository.KullaniciRepository;
import com.adaloveladies.SpringProjesi.repository.SehirRepository;
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
    private final KullaniciRepository kullaniciRepository;
    private final SehirRepository sehirRepository;

    @Transactional
    public Building binaOlustur(Long kullaniciId, Long sehirId) {
        Kullanici kullanici = kullaniciRepository.findById(kullaniciId)
            .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        
        Sehir sehir = sehirRepository.findById(sehirId)
            .orElseThrow(() -> new RuntimeException("Şehir bulunamadı"));

        Building bina = Building.builder()
            .kullanici(kullanici)
            .sehir(sehir)
            .katSayisi(0)
            .tamamlandi(false)
            .hasRoof(false)
            .gunlukTamamlananGorevSayisi(0)
            .gerekliSeviye(1)
            .build();

        return buildingRepository.save(bina);
    }

    @Transactional
    public Building binaGuncelle(Long binaId, int katSayisi, boolean tamamlandi, boolean hasRoof) {
        Building bina = buildingRepository.findById(binaId)
            .orElseThrow(() -> new RuntimeException("Bina bulunamadı"));

        bina.setKatSayisi(katSayisi);
        bina.setTamamlandi(tamamlandi);
        bina.setHasRoof(hasRoof);

        return buildingRepository.save(bina);
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