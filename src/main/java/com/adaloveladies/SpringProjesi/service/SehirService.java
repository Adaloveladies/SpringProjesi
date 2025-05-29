package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.model.Sehir;
import com.adaloveladies.SpringProjesi.repository.SehirRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SehirService {

    private final SehirRepository sehirRepository;
    private final KullaniciService kullaniciService;
    
    private static final int SEVIYE_ATLAMA_PUANI = 100;

    public Sehir sehirOlustur(Long kullaniciId) {
        Kullanici kullanici = kullaniciService.findById(kullaniciId);
        
        Sehir sehir = Sehir.builder()
                .ad("Yeni Şehir")
                .seviye(0)
                .toplamPuan(0)
                .gorunum("sehir_0.png")
                .kullanici(kullanici)
                .aktif(true)
                .sonrakiSeviyePuani(SEVIYE_ATLAMA_PUANI)
                .build();

        return sehirRepository.save(sehir);
    }

    public Sehir sehirGetir(Long kullaniciId) {
        return sehirRepository.findByKullaniciId(kullaniciId)
                .orElseGet(() -> sehirOlustur(kullaniciId));
    }

    public void puanEkle(Long kullaniciId, Integer puan) {
        Sehir sehir = sehirGetir(kullaniciId);
        sehir.setToplamPuan(sehir.getToplamPuan() + puan);
        
        // Her seviye için gereken toplam puan: (seviye + 1) * 100
        int sonrakiSeviyePuani = (sehir.getSeviye() + 1) * SEVIYE_ATLAMA_PUANI;
        sehir.setSonrakiSeviyePuani(sonrakiSeviyePuani);
        
        if (sehir.seviyeAtlayabilirMi()) {
            sehir.seviyeAtla();
        }
        
        sehirRepository.save(sehir);
    }

    public boolean seviyeAtlayabilirMi(Long kullaniciId) {
        Sehir sehir = sehirGetir(kullaniciId);
        return sehir.seviyeAtlayabilirMi();
    }
} 