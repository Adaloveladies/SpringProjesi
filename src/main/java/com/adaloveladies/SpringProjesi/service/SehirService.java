package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.model.Sehir;
import com.adaloveladies.SpringProjesi.repository.SehirRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SehirService {

    private final SehirRepository sehirRepository;

    public Sehir sehirOlustur(Kullanici kullanici) {
        Sehir sehir = Sehir.builder()
                .name(kullanici.getUsername() + "'in Şehri")
                .description("Yeni kurulmuş bir şehir")
                .kullanici(kullanici)
                .build();
        return sehirRepository.save(sehir);
    }

    public Sehir sehirGuncelle(Long id, String name, String description) {
        Sehir sehir = sehirRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Şehir bulunamadı"));
        sehir.setName(name);
        sehir.setDescription(description);
        return sehirRepository.save(sehir);
    }

    public void sehirSil(Long id) {
        sehirRepository.deleteById(id);
    }

    public List<Sehir> tumSehirleriGetir() {
        return sehirRepository.findAll();
    }

    public Optional<Sehir> sehirGetir(Long id) {
        return sehirRepository.findById(id);
    }

    public Optional<Sehir> kullaniciSehriGetir(Kullanici kullanici) {
        return sehirRepository.findByKullanici(kullanici);
    }

    public void puanEkle(Kullanici kullanici, int points) {
        Sehir sehir = kullaniciSehriGetir(kullanici)
                .orElseThrow(() -> new RuntimeException("Kullanıcının şehri bulunamadı"));
        sehir.addPoints(points);
        sehirRepository.save(sehir);
    }

    public void seviyeAtla(Long id) {
        Sehir sehir = sehirRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Şehir bulunamadı"));
        if (sehir.canLevelUp()) {
            sehir.levelUp();
            sehirRepository.save(sehir);
        }
    }

    public boolean seviyeAtlayabilirMi(Long kullaniciId) {
        return sehirRepository.findByKullaniciId(kullaniciId)
                .map(Sehir::canLevelUp)
                .orElse(false);
    }
} 