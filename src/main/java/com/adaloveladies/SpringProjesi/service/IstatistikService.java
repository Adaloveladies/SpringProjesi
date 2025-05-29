package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.model.Istatistik;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.model.GorevDurumu;
import com.adaloveladies.SpringProjesi.repository.IstatistikRepository;
import com.adaloveladies.SpringProjesi.repository.GorevRepository;
import com.adaloveladies.SpringProjesi.repository.RozetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

@Service
@Transactional
@RequiredArgsConstructor
public class IstatistikService {
    
    private final IstatistikRepository istatistikRepository;
    private final GorevRepository gorevRepository;
    private final RozetRepository rozetRepository;
    
    @Transactional
    @CacheEvict(value = "istatistikler", key = "#kullanici.id")
    public void istatistikleriGuncelle(Kullanici kullanici) {
        var istatistik = istatistikRepository.findByKullanici(kullanici)
            .orElse(new Istatistik(kullanici));
            
        // Görev istatistikleri
        istatistik.setToplamGorevSayisi((int) gorevRepository.countByKullanici(kullanici));
        istatistik.setTamamlananGorevSayisi((int) gorevRepository.countByKullaniciAndDurum(kullanici, GorevDurumu.TAMAMLANDI));
        
        // Zaman bazlı görev sayıları
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime gunBasi = now.toLocalDate().atStartOfDay();
        LocalDateTime haftaBasi = now.minusDays(now.getDayOfWeek().getValue() - 1).toLocalDate().atStartOfDay();
        LocalDateTime ayBasi = now.withDayOfMonth(1).toLocalDate().atStartOfDay();
        
        istatistik.setGunlukGorevSayisi((int) gorevRepository.countByKullaniciAndDurumAndTamamlanmaTarihiBetween(
            kullanici, GorevDurumu.TAMAMLANDI, gunBasi, now));
        istatistik.setHaftalikGorevSayisi((int) gorevRepository.countByKullaniciAndDurumAndTamamlanmaTarihiBetween(
            kullanici, GorevDurumu.TAMAMLANDI, haftaBasi, now));
        istatistik.setAylikGorevSayisi((int) gorevRepository.countByKullaniciAndDurumAndTamamlanmaTarihiBetween(
            kullanici, GorevDurumu.TAMAMLANDI, ayBasi, now));
        
        // Puan ve rozet istatistikleri
        istatistik.setToplamPuan(kullanici.getPuan());
        istatistik.setKazanilanRozetSayisi((int) rozetRepository.countByKullanici(kullanici));
        
        istatistikRepository.save(istatistik);
    }
    
    @Cacheable(value = "genelIstatistikler")
    public Map<String, Object> getGenelIstatistikler() {
        double ortalamaBasariOrani = istatistikRepository.calculateAverageSuccessRate();
        long toplamKullanici = istatistikRepository.count();
        long yuksekBasariOraniKullanici = istatistikRepository.countByBasariOraniGreaterThan(80.0);

        return Map.of(
            "ortalamaBasariOrani", ortalamaBasariOrani,
            "toplamKullanici", toplamKullanici,
            "yuksekBasariOraniKullanici", yuksekBasariOraniKullanici
        );
    }
    
    @Cacheable(value = "istatistikler", key = "#kullanici.id")
    public Istatistik getKullaniciIstatistikleri(Kullanici kullanici) {
        return istatistikRepository.findByKullanici(kullanici)
                .orElseGet(() -> {
                    Istatistik istatistik = new Istatistik();
                    istatistik.setKullanici(kullanici);
                    return istatistikRepository.save(istatistik);
                });
    }

    public List<Map<String, Object>> getEnAktifKullanicilar(int limit) {
        return istatistikRepository.findTopByOrderByToplamGorevSayisiDesc(PageRequest.of(0, limit))
                .stream()
                .map(istatistik -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("kullaniciId", istatistik.getKullanici().getId());
                    map.put("kullaniciAdi", istatistik.getKullanici().getUsername());
                    map.put("toplamGorev", istatistik.getToplamGorevSayisi());
                    map.put("basariOrani", istatistik.getBasariOrani());
                    return map;
                })
                .collect(Collectors.toList());
    }

    public Map<String, Object> getGunlukIstatistikler() {
        LocalDateTime bugun = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        long bugunTamamlananGorev = istatistikRepository.countByTamamlanmaTarihiAfter(bugun);
        long bugunYeniKullanici = istatistikRepository.countByKayitTarihiAfter(bugun);

        return Map.of(
            "bugunTamamlananGorev", bugunTamamlananGorev,
            "bugunYeniKullanici", bugunYeniKullanici
        );
    }
} 