package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.exception.ResourceNotFoundException;
import com.adaloveladies.SpringProjesi.model.Bildirim;
import com.adaloveladies.SpringProjesi.model.Gorev;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.repository.BildirimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BildirimService {

    private final BildirimRepository bildirimRepository;

    public void bildirimGonder(Kullanici kullanici, String baslik, String mesaj, Bildirim.BildirimTipi tip) {
        Bildirim bildirim = Bildirim.builder()
                .kullanici(kullanici)
                .baslik(baslik)
                .mesaj(mesaj)
                .tip(tip)
                .okundu(false)
                .olusturmaTarihi(LocalDateTime.now())
                .build();

        bildirimRepository.save(bildirim);
    }

    public void bildirimGonder(Kullanici kullanici, String mesaj) {
        bildirimGonder(kullanici, "Bildirim", mesaj, Bildirim.BildirimTipi.GENEL);
    }

    public List<Bildirim> kullaniciBildirimleriniGetir(Kullanici kullanici) {
        return bildirimRepository.findByKullaniciOrderByOlusturmaTarihiDesc(kullanici);
    }

    public void bildirimiOkunduOlarakIsaretle(Long bildirimId) {
        Bildirim bildirim = bildirimRepository.findById(bildirimId)
                .orElseThrow(() -> new ResourceNotFoundException("Bildirim", "id", bildirimId));
        bildirim.setOkundu(true);
        bildirimRepository.save(bildirim);
    }

    public void tumBildirimleriOkunduOlarakIsaretle(Kullanici kullanici) {
        List<Bildirim> bildirimler = bildirimRepository.findByKullaniciAndOkunduFalse(kullanici);
        bildirimler.forEach(bildirim -> bildirim.setOkundu(true));
        bildirimRepository.saveAll(bildirimler);
    }

    public void gorevOlusturulduBildirimiGonder(Kullanici kullanici, Gorev gorev) {
        Bildirim bildirim = Bildirim.builder()
            .kullanici(kullanici)
            .baslik("Yeni Görev Oluşturuldu")
            .mesaj(gorev.getBaslik() + " görevi oluşturuldu.")
            .tip(Bildirim.BildirimTipi.GOREV)
            .okundu(false)
            .olusturmaTarihi(LocalDateTime.now())
            .build();
        bildirimRepository.save(bildirim);
    }

    public void gorevTamamlandiBildirimiGonder(Kullanici kullanici, Gorev gorev) {
        Bildirim bildirim = Bildirim.builder()
            .kullanici(kullanici)
            .baslik("Görev Tamamlandı")
            .mesaj(gorev.getBaslik() + " görevi tamamlandı. " + gorev.getPuanDegeri() + " puan kazandınız!")
            .tip(Bildirim.BildirimTipi.GOREV)
            .okundu(false)
            .olusturmaTarihi(LocalDateTime.now())
            .build();
        bildirimRepository.save(bildirim);
    }
} 