package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.model.Rozet;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.model.Bildirim;
import com.adaloveladies.SpringProjesi.repository.RozetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RozetService {
    
    private final RozetRepository rozetRepository;
    private final BildirimService bildirimService;
    
    @SuppressWarnings("incomplete-switch")
	@Transactional
    public void puanKontroluVeRozetVer(Kullanici kullanici) {
        int puan = kullanici.getPoints();
        Rozet.RozetTipi yeniTip = null;
        
        // Puan kontrolü ve rozet tipi belirleme
        if (puan >= 100 && !rozetRepository.existsByKullaniciAndTip(kullanici, Rozet.RozetTipi.PUAN)) {
            yeniTip = Rozet.RozetTipi.PUAN;
        } else if (kullanici.getSeviye() >= 5 && !rozetRepository.existsByKullaniciAndTip(kullanici, Rozet.RozetTipi.SEVIYE)) {
            yeniTip = Rozet.RozetTipi.SEVIYE;
        } else if (rozetRepository.countByKullanici(kullanici) >= 10 && !rozetRepository.existsByKullaniciAndTip(kullanici, Rozet.RozetTipi.GOREV_SAYISI)) {
            yeniTip = Rozet.RozetTipi.GOREV_SAYISI;
        }
        
        if (yeniTip != null) {
            Rozet rozet = Rozet.builder()
                .kullanici(kullanici)
                .tip(yeniTip)
                .build();
            
            // Rozet bilgilerini ayarla
            switch (yeniTip) {
                case PUAN:
                    rozet.setAd("Puan Ustası");
                    rozet.setAciklama("100 puanına ulaştın!");
                    break;
                case SEVIYE:
                    rozet.setAd("Seviye Ustası");
                    rozet.setAciklama("5. seviyeye ulaştın!");
                    break;
                case GOREV_SAYISI:
                    rozet.setAd("Görev Ustası");
                    rozet.setAciklama("10 görevi tamamladın!");
                    break;
                case OZEL:
                    rozet.setAd("Özel Başarı");
                    rozet.setAciklama("Özel bir başarıya ulaştın!");
                    break;
            }
            
            rozetRepository.save(rozet);
            
            // Bildirim gönder
            bildirimService.bildirimGonder(
                kullanici,
                "Yeni Rozet Kazandın!",
                rozet.getAd() + " rozetini kazandın! " + rozet.getAciklama(),
                Bildirim.BildirimTipi.ROZET
            );
        }
    }
    
    public List<Rozet> kullaniciRozetleriniGetir(Kullanici kullanici) {
        return rozetRepository.findByKullanici(kullanici);
    }
} 