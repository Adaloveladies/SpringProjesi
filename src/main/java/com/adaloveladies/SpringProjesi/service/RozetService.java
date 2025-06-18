package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.model.Rozet;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.model.Bildirim;
import com.adaloveladies.SpringProjesi.repository.RozetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class RozetService {
    
    private final RozetRepository rozetRepository;
    private final BildirimService bildirimService;
    
    @Transactional
    public void puanKontroluVeRozetVer(Kullanici kullanici) {
        int puan = kullanici.getPoints();
        Rozet.RozetTipi yeniTip = null;
        String rozetAdi = null;
        String rozetAciklama = null;
        
        // Puan kontrolü ve rozet tipi belirleme
        if (puan >= 100 && !rozetRepository.existsByKullaniciAndTip(kullanici, Rozet.RozetTipi.PUAN)) {
            yeniTip = Rozet.RozetTipi.PUAN;
            rozetAdi = "Puan Ustası";
            rozetAciklama = "100 puanına ulaştın!";
        } else if (kullanici.getLevel() >= 5 && !rozetRepository.existsByKullaniciAndTip(kullanici, Rozet.RozetTipi.SEVIYE)) {
            yeniTip = Rozet.RozetTipi.SEVIYE;
            rozetAdi = "Seviye Ustası";
            rozetAciklama = "5. seviyeye ulaştın!";
        } else if (rozetRepository.countByKullanici(kullanici) >= 10 && !rozetRepository.existsByKullaniciAndTip(kullanici, Rozet.RozetTipi.GOREV_SAYISI)) {
            yeniTip = Rozet.RozetTipi.GOREV_SAYISI;
            rozetAdi = "Görev Ustası";
            rozetAciklama = "10 görevi tamamladın!";
        }
        
        if (yeniTip != null) {
            Rozet rozet = Rozet.builder()
                .kullanici(kullanici)
                .tip(yeniTip)
                .ad(rozetAdi)
                .aciklama(rozetAciklama)
                .ikon(getIkonForRozet(yeniTip))
                .puanDegeri(getPuanDegeriForRozet(yeniTip))
                .kazanilmaTarihi(LocalDateTime.now())
                .build();
            
            rozetRepository.save(rozet);
            
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

    @Transactional
    public void gorevTamamlandiKontrol(Kullanici kullanici) {
        int tamamlananGorevSayisi = kullanici.getCompletedTaskCount();
        
        boolean ustasiVar = kullanici.getRozetler().stream()
            .anyMatch(r -> r.getTip() == Rozet.RozetTipi.GOREV_USTASI);
        boolean efendisiVar = kullanici.getRozetler().stream()
            .anyMatch(r -> r.getTip() == Rozet.RozetTipi.GOREV_EFENDISI);
        
        if (tamamlananGorevSayisi >= 10 && !ustasiVar) {
            Rozet rozet = Rozet.builder()
                .kullanici(kullanici)
                .tip(Rozet.RozetTipi.GOREV_USTASI)
                .ad("Görev Ustası")
                .aciklama("10 görev tamamladın!")
                .ikon("🏆")
                .puanDegeri(50)
                .kazanilmaTarihi(LocalDateTime.now())
                .build();
            
            rozetRepository.save(rozet);
            
            bildirimService.bildirimGonder(
                kullanici,
                "Yeni Rozet Kazandın!",
                "Görev Ustası rozetini kazandın! 10 görev tamamladın!",
                Bildirim.BildirimTipi.ROZET
            );
        }
        
        if (tamamlananGorevSayisi >= 50 && !efendisiVar) {
            Rozet rozet = Rozet.builder()
                .kullanici(kullanici)
                .tip(Rozet.RozetTipi.GOREV_EFENDISI)
                .ad("Görev Efendisi")
                .aciklama("50 görev tamamladın!")
                .ikon("👑")
                .puanDegeri(100)
                .kazanilmaTarihi(LocalDateTime.now())
                .build();
            
            rozetRepository.save(rozet);
            
            bildirimService.bildirimGonder(
                kullanici,
                "Yeni Rozet Kazandın!",
                "Görev Efendisi rozetini kazandın! 50 görev tamamladın!",
                Bildirim.BildirimTipi.ROZET
            );
        }
    }
    
    private String getIkonForRozet(Rozet.RozetTipi tip) {
        switch (tip) {
            case PUAN: return "💰";
            case SEVIYE: return "⭐";
            case GOREV_SAYISI: return "📊";
            case GOREV_USTASI: return "🏆";
            case GOREV_EFENDISI: return "👑";
            case OZEL: return "🌟";
            default: return "🎖️";
        }
    }
    
    private int getPuanDegeriForRozet(Rozet.RozetTipi tip) {
        switch (tip) {
            case PUAN: return 50;
            case SEVIYE: return 75;
            case GOREV_SAYISI: return 100;
            case GOREV_USTASI: return 150;
            case GOREV_EFENDISI: return 200;
            case OZEL: return 250;
            default: return 0;
        }
    }
} 