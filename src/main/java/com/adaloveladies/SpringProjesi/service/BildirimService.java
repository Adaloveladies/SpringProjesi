package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.model.Bildirim;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.repository.BildirimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BildirimService {

    private final BildirimRepository bildirimRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void bildirimGonder(Kullanici kullanici, String baslik, String mesaj, Bildirim.BildirimTipi tip) {
        Bildirim bildirim = new Bildirim();
        bildirim.setKullanici(kullanici);
        bildirim.setBaslik(baslik);
        bildirim.setMesaj(mesaj);
        bildirim.setBildirimTipi(tip);
        bildirimRepository.save(bildirim);

        // WebSocket üzerinden anlık bildirim gönder
        messagingTemplate.convertAndSendToUser(
            kullanici.getId().toString(),
            "/queue/bildirimler",
            bildirim
        );
    }

    public List<Bildirim> getKullaniciBildirimleri(Long kullaniciId) {
        return bildirimRepository.findByKullaniciIdOrderByOlusturmaTarihiDesc(kullaniciId);
    }

    public List<Bildirim> getOkunmamisBildirimler(Long kullaniciId) {
        return bildirimRepository.findByKullaniciIdAndOkunduFalseOrderByOlusturmaTarihiDesc(kullaniciId);
    }

    @Transactional
    public void bildirimOkunduIsaretle(Long bildirimId) {
        Bildirim bildirim = bildirimRepository.findById(bildirimId)
                .orElseThrow(() -> new RuntimeException("Bildirim bulunamadı"));
        bildirim.setOkundu(true);
        bildirimRepository.save(bildirim);
    }

    public Long getOkunmamisBildirimSayisi(Long kullaniciId) {
        return bildirimRepository.countByKullaniciIdAndOkunduFalse(kullaniciId);
    }
} 