package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.model.Istatistik;
import com.adaloveladies.SpringProjesi.model.Kullanici;
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
        Istatistik istatistik = getKullaniciIstatistikleri(kullanici);
        istatistik.setTamamlananGorevSayisi((int) gorevRepository.findByKullaniciIdAndTamamlandi(kullanici.getId(), true).size());
        istatistik.setToplamPuan(kullanici.getPoints());
        istatistik.setSeviye(kullanici.getLevel());
        istatistik.setBasarimSayisi((int) rozetRepository.countByKullanici(kullanici));
        istatistikRepository.save(istatistik);
    }
    
    @Cacheable(value = "genelIstatistikler")
    public Map<String, Object> getGenelIstatistikler() {
        Map<String, Object> istatistikler = new HashMap<>();
        istatistikler.put("toplamKullanici", istatistikRepository.count());
        istatistikler.put("toplamGorev", gorevRepository.count());
        istatistikler.put("toplamPuan", istatistikRepository.sumToplamPuan());
        return istatistikler;
    }
    
    @Cacheable(value = "istatistikler", key = "#kullanici.id")
    public Istatistik getKullaniciIstatistikleri(Kullanici kullanici) {
        return istatistikRepository.findByKullanici(kullanici)
                .orElseGet(() -> Istatistik.builder()
                        .kullanici(kullanici)
                        .tamamlananGorevSayisi(0)
                        .toplamPuan(0)
                        .seviye(1)
                        .basarimSayisi(0)
                        .build());
    }

    public List<Map<String, Object>> getEnAktifKullanicilar(int limit) {
        return istatistikRepository.findTopByOrderByTamamlananGorevSayisiDesc(PageRequest.of(0, limit))
                .stream()
                .map(istatistik -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("kullaniciId", istatistik.getKullanici().getId());
                    map.put("kullaniciAdi", istatistik.getKullanici().getUsername());
                    map.put("toplamGorev", istatistik.getTamamlananGorevSayisi());
                    map.put("basariOrani", calculateBasariOrani(istatistik));
                    return map;
                })
                .collect(Collectors.toList());
    }

    private double calculateBasariOrani(Istatistik istatistik) {
        long toplamGorev = gorevRepository.findByKullaniciId(istatistik.getKullanici().getId()).size();
        return toplamGorev > 0 ? (double) istatistik.getTamamlananGorevSayisi() / toplamGorev * 100 : 0;
    }

    public Map<String, Object> getGunlukIstatistikler() {
        LocalDateTime bugun = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        long bugunTamamlananGorev = gorevRepository.findByTamamlandiAndTamamlanmaTarihiAfter(true, bugun).size();
        long bugunYeniKullanici = istatistikRepository.countByKayitTarihiAfter(bugun);

        return Map.of(
            "bugunTamamlananGorev", bugunTamamlananGorev,
            "bugunYeniKullanici", bugunYeniKullanici
        );
    }
} 