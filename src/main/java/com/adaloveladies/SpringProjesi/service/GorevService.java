package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.dto.GorevRequestDTO;
import com.adaloveladies.SpringProjesi.dto.GorevResponseDTO;
import com.adaloveladies.SpringProjesi.exception.BusinessException;
import com.adaloveladies.SpringProjesi.exception.ResourceNotFoundException;
import com.adaloveladies.SpringProjesi.model.*;
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
        Kullanici kullanici = kullaniciRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı", "kullanıcı adı", username));

        long bugunTamamlananGorevSayisi = gorevRepository.countByKullaniciAndDurumAndTamamlanmaTarihiBetween(
            kullanici, 
            TaskStatus.TAMAMLANDI,
            LocalDate.now().atStartOfDay(),
            LocalDate.now().atTime(23, 59, 59)
        );
        
        if (bugunTamamlananGorevSayisi >= GUNLUK_GOREV_LIMITI) {
            throw new BusinessException("Günlük görev limitine ulaştınız. Yarın tekrar deneyin.");
        }

        Task gorev = Task.builder()
                .baslik(requestDTO.getBaslik())
                .aciklama(requestDTO.getAciklama())
                .puanDegeri(calculatePoints(requestDTO.getGorevTipi()))
                .sonTarih(requestDTO.getSonTarih())
                .gorevTipi(requestDTO.getGorevTipi())
                .kullanici(kullanici)
                .durum(TaskStatus.BEKLEMEDE)
                .build();

        gorev = gorevRepository.save(gorev);
        bildirimService.bildirimGonder(
            kullanici,
            "Yeni Görev Oluşturuldu",
            gorev.getBaslik() + " görevi oluşturuldu.",
            Bildirim.BildirimTipi.GOREV
        );
        
        return gorevToResponseDTO(gorev);
    }

    private int calculatePoints(GorevTipi gorevTipi) {
        switch (gorevTipi) {
            case GUNLUK:
                return 10;
            case HAFTALIK:
                return 50;
            case AYLIK:
                return 200;
            case OZEL:
                return 100;
            default:
                return 0;
        }
    }

    public List<GorevResponseDTO> gorevleriListele(String username) {
        Kullanici kullanici = kullaniciRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı", "kullanıcı adı", username));
        return gorevRepository.findByKullanici(kullanici).stream()
                .map(this::gorevToResponseDTO)
                .collect(Collectors.toList());
    }

    public GorevResponseDTO gorevDetay(Long id, String username) {
        Kullanici kullanici = kullaniciRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı", "kullanıcı adı", username));
        Task gorev = gorevRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Görev", "id", id));
        if (!gorev.getKullanici().equals(kullanici)) {
            throw new BusinessException("Bu göreve erişim yetkiniz yok");
        }
        return gorevToResponseDTO(gorev);
    }

    public GorevResponseDTO gorevGuncelle(Long id, GorevRequestDTO requestDTO, String username) {
        Kullanici kullanici = kullaniciRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı", "kullanıcı adı", username));
        Task gorev = gorevRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Görev", "id", id));
        if (!gorev.getKullanici().equals(kullanici)) {
            throw new BusinessException("Bu görevi güncelleme yetkiniz yok");
        }
        gorev.setBaslik(requestDTO.getBaslik());
        gorev.setAciklama(requestDTO.getAciklama());
        gorev.setPuanDegeri(calculatePoints(requestDTO.getGorevTipi()));
        gorev.setSonTarih(requestDTO.getSonTarih());
        gorev.setGorevTipi(requestDTO.getGorevTipi());
        return gorevToResponseDTO(gorevRepository.save(gorev));
    }

    public void gorevSil(Long id, String username) {
        Kullanici kullanici = kullaniciRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı", "kullanıcı adı", username));
        Task gorev = gorevRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Görev", "id", id));
        if (!gorev.getKullanici().equals(kullanici)) {
            throw new BusinessException("Bu görevi silme yetkiniz yok");
        }
        gorevRepository.delete(gorev);
    }

    @Transactional
    public GorevResponseDTO gorevTamamla(Long gorevId, String username) {
        Kullanici kullanici = kullaniciRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı", "kullanıcı adı", username));
        Task gorev = gorevRepository.findById(gorevId)
            .orElseThrow(() -> new ResourceNotFoundException("Görev", "id", gorevId));
        if (!gorev.getKullanici().equals(kullanici)) {
            throw new BusinessException("Bu görevi tamamlama yetkiniz yok");
        }
        if (gorev.getDurum() == TaskStatus.TAMAMLANDI) {
            throw new BusinessException("Görev zaten tamamlanmış");
        }
        gorev.setDurum(TaskStatus.TAMAMLANDI);
        gorev.setTamamlanmaTarihi(LocalDateTime.now());
        
        kullanici.setPoints(kullanici.getPoints() + gorev.getPuanDegeri());
        kullanici.setCompletedTaskCount(kullanici.getCompletedTaskCount() + 1);
        
        int yeniSeviye = (kullanici.getPoints() / 100) + 1;
        if (yeniSeviye > kullanici.getLevel()) {
            kullanici.setLevel(yeniSeviye);
            bildirimService.bildirimGonder(
                kullanici,
                "Tebrikler!",
                "Seviye " + yeniSeviye + "'e yükseldin!",
                Bildirim.BildirimTipi.SEVIYE_ATLAMA
            );
        }
        
        rozetService.puanKontroluVeRozetVer(kullanici);
        
        kullaniciRepository.save(kullanici);
        return gorevToResponseDTO(gorevRepository.save(gorev));
    }

    private GorevResponseDTO gorevToResponseDTO(Task gorev) {
        return GorevResponseDTO.builder()
                .id(gorev.getId())
                .baslik(gorev.getBaslik())
                .aciklama(gorev.getAciklama())
                .puanDegeri(gorev.getPuanDegeri())
                .sonTarih(gorev.getSonTarih())
                .durum(gorev.getDurum())
                .gorevTipi(gorev.getGorevTipi())
                .kullaniciId(gorev.getKullanici().getId())
                .username(gorev.getKullanici().getUsername())
                .olusturmaTarihi(gorev.getOlusturmaTarihi())
                .tamamlanmaTarihi(gorev.getTamamlanmaTarihi())
                .build();
    }
} 