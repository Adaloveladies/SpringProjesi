package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.dto.GorevRequestDTO;
import com.adaloveladies.SpringProjesi.dto.GorevResponseDTO;
import com.adaloveladies.SpringProjesi.model.Gorev;
import com.adaloveladies.SpringProjesi.model.GorevDurumu;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.model.Bildirim;
import com.adaloveladies.SpringProjesi.repository.GorevRepository;
import com.adaloveladies.SpringProjesi.repository.KullaniciRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GorevService {

    private final GorevRepository gorevRepository;
    private final KullaniciRepository kullaniciRepository;
    private final BildirimService bildirimService;
    private final RozetService rozetService;
    
    private static final int GUNLUK_GOREV_LIMITI = 20;

    public GorevResponseDTO gorevOlustur(GorevRequestDTO requestDTO, String username) {
        Kullanici kullanici = kullaniciRepository.findByKullaniciAdi(username)
            .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        // Günlük görev limitini kontrol et
        long bugunTamamlananGorevSayisi = gorevRepository.countByKullaniciAndDurumAndTamamlanmaTarihiBetween(
            kullanici, 
            GorevDurumu.TAMAMLANDI,
            LocalDate.now().atStartOfDay(),
            LocalDate.now().atTime(23, 59, 59)
        );
        if (bugunTamamlananGorevSayisi >= GUNLUK_GOREV_LIMITI) {
            throw new RuntimeException("Günlük görev limitine ulaştınız. Yarın tekrar deneyin.");
        }
        Gorev gorev = Gorev.builder()
                .baslik(requestDTO.getBaslik())
                .aciklama(requestDTO.getAciklama())
                .puanDegeri(requestDTO.getTip().getPuanDegeri())
                .sonTarih(requestDTO.getSonTarih())
                .tip(requestDTO.getTip())
                .kullanici(kullanici)
                .durum(GorevDurumu.AKTIF)
                .build();
        return gorevToResponseDTO(gorevRepository.save(gorev));
    }

    public List<GorevResponseDTO> gorevleriListele(String username) {
        Kullanici kullanici = kullaniciRepository.findByKullaniciAdi(username)
            .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        return gorevRepository.findByKullanici(kullanici).stream()
                .map(this::gorevToResponseDTO)
                .collect(Collectors.toList());
    }

    public GorevResponseDTO gorevDetay(Long id, String username) {
        Kullanici kullanici = kullaniciRepository.findByKullaniciAdi(username)
            .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        Gorev gorev = gorevRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Görev bulunamadı"));
        if (!gorev.getKullanici().equals(kullanici)) {
            throw new RuntimeException("Bu göreve erişim yetkiniz yok");
        }
        return gorevToResponseDTO(gorev);
    }

    public GorevResponseDTO gorevGuncelle(Long id, GorevRequestDTO requestDTO, String username) {
        Kullanici kullanici = kullaniciRepository.findByKullaniciAdi(username)
            .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        Gorev gorev = gorevRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Görev bulunamadı"));
        if (!gorev.getKullanici().equals(kullanici)) {
            throw new RuntimeException("Bu görevi güncelleme yetkiniz yok");
        }
        gorev.setBaslik(requestDTO.getBaslik());
        gorev.setAciklama(requestDTO.getAciklama());
        gorev.setPuanDegeri(requestDTO.getTip().getPuanDegeri());
        gorev.setSonTarih(requestDTO.getSonTarih());
        gorev.setTip(requestDTO.getTip());
        return gorevToResponseDTO(gorevRepository.save(gorev));
    }

    public void gorevSil(Long id, String username) {
        Kullanici kullanici = kullaniciRepository.findByKullaniciAdi(username)
            .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        Gorev gorev = gorevRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Görev bulunamadı"));
        if (!gorev.getKullanici().equals(kullanici)) {
            throw new RuntimeException("Bu görevi silme yetkiniz yok");
        }
        gorevRepository.delete(gorev);
    }

    @Transactional
    public GorevResponseDTO gorevTamamla(Long gorevId, String username) {
        Kullanici kullanici = kullaniciRepository.findByKullaniciAdi(username)
            .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        Gorev gorev = gorevRepository.findById(gorevId)
            .orElseThrow(() -> new RuntimeException("Görev bulunamadı"));
        if (!gorev.getKullanici().equals(kullanici)) {
            throw new RuntimeException("Bu görevi tamamlama yetkiniz yok");
        }
        if (gorev.getDurum() == GorevDurumu.TAMAMLANDI) {
            throw new RuntimeException("Görev zaten tamamlanmış");
        }
        gorev.setDurum(GorevDurumu.TAMAMLANDI);
        gorev.setTamamlanmaTarihi(LocalDateTime.now());
        // Puan ekle (görev tipine göre)
        kullanici.setPuan(kullanici.getPuan() + gorev.getPuanDegeri());
        // Seviye kontrolü
        int yeniSeviye = (kullanici.getPuan() / 100) + 1;
        if (yeniSeviye > kullanici.getSeviye()) {
            kullanici.setSeviye(yeniSeviye);
            bildirimService.bildirimGonder(
                kullanici,
                "Tebrikler!",
                "Seviye " + yeniSeviye + "'e yükseldin!",
                Bildirim.BildirimTipi.SEVIYE_ATLANDI
            );
        }
        // Rozet kontrolü
        rozetService.puanKontroluVeRozetVer(kullanici);
        // Rutin kontrolü ve yeni görev oluşturma
        if (gorev.getRutinOlustur() != null && gorev.getRutinOlustur() && gorev.getTekrar() != null) {
            rutinGorevOlustur(gorev, kullanici);
        }
        kullaniciRepository.save(kullanici);
        return gorevToResponseDTO(gorevRepository.save(gorev));
    }

    private void rutinGorevOlustur(Gorev tamamlananGorev, Kullanici kullanici) {
        LocalDateTime yeniSonTarih = null;
        switch (tamamlananGorev.getTekrar()) {
            case GUNLUK:
                yeniSonTarih = LocalDateTime.now().plusDays(1);
                break;
            case HAFTALIK:
                yeniSonTarih = LocalDateTime.now().plusWeeks(1);
                break;
            case AYLIK:
                yeniSonTarih = LocalDateTime.now().plusMonths(1);
                break;
            default:
                return;
        }
        Gorev yeniGorev = Gorev.builder()
            .baslik(tamamlananGorev.getBaslik())
            .aciklama(tamamlananGorev.getAciklama())
            .puanDegeri(tamamlananGorev.getPuanDegeri())
            .sonTarih(yeniSonTarih)
            .tip(tamamlananGorev.getTip())
            .tekrar(tamamlananGorev.getTekrar())
            .rutinOlustur(tamamlananGorev.getRutinOlustur())
            .kullanici(kullanici)
            .durum(GorevDurumu.AKTIF)
            .build();
        gorevRepository.save(yeniGorev);
        bildirimService.bildirimGonder(
            kullanici,
            "Yeni Rutin Görev Oluşturuldu",
            tamamlananGorev.getBaslik() + " görevi için yeni bir rutin oluşturuldu. Son tarih: " + yeniSonTarih,
            Bildirim.BildirimTipi.SISTEM_UYARI
        );
    }

    private GorevResponseDTO gorevToResponseDTO(Gorev gorev) {
        return GorevResponseDTO.builder()
                .id(gorev.getId())
                .baslik(gorev.getBaslik())
                .aciklama(gorev.getAciklama())
                .puanDegeri(gorev.getPuanDegeri())
                .sonTarih(gorev.getSonTarih())
                .durum(gorev.getDurum())
                .tip(gorev.getTip())
                .kullaniciId(gorev.getKullanici().getId())
                .kullaniciAdi(gorev.getKullanici().getKullaniciAdi())
                .olusturmaTarihi(gorev.getOlusturmaTarihi())
                .tamamlanmaTarihi(gorev.getTamamlanmaTarihi())
                .build();
    }
} 