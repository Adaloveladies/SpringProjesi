package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.dto.GorevRequestDTO;
import com.adaloveladies.SpringProjesi.dto.GorevResponseDTO;
import com.adaloveladies.SpringProjesi.exception.BusinessException;
import com.adaloveladies.SpringProjesi.exception.ResourceNotFoundException;
import com.adaloveladies.SpringProjesi.model.Building;
import com.adaloveladies.SpringProjesi.model.Gorev;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.model.Sehir;
import com.adaloveladies.SpringProjesi.repository.BuildingRepository;
import com.adaloveladies.SpringProjesi.repository.GorevRepository;
import com.adaloveladies.SpringProjesi.repository.KullaniciRepository;
import com.adaloveladies.SpringProjesi.repository.SehirRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GorevService {

    private final GorevRepository gorevRepository;
    private final KullaniciRepository kullaniciRepository;
    private final SehirRepository sehirRepository;
    private final BildirimService bildirimService;
    private final RozetService rozetService;
    private final BuildingRepository buildingRepository;
    @SuppressWarnings("unused")
	private final KullaniciService kullaniciService;
    
    private static final int GUNLUK_GOREV_LIMITI = 20;
    private static final int GOREV_PUAN_DEGERI = 10;
    private static final int BINA_TAMAMLAMA_PUANI = 100;
    private static final int MAKSIMUM_KAT_SAYISI = 10;

    public GorevResponseDTO gorevOlustur(GorevRequestDTO requestDTO, String username) {
        Kullanici kullanici = kullaniciRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı", "kullanıcı adı", username));

        if (requestDTO.getBaslik() == null || requestDTO.getBaslik().trim().isEmpty()) {
            throw new BusinessException("Görev başlığı boş olamaz");
        }

        if (requestDTO.getPuanDegeri() <= 0) {
            throw new BusinessException("Görev puan değeri pozitif olmalıdır");
        }

        long gunlukGorevSayisi = gorevRepository.findByKullaniciIdAndTamamlandi(kullanici.getId(), true).size();
        if (gunlukGorevSayisi >= GUNLUK_GOREV_LIMITI) {
            throw new BusinessException("Günlük görev limitine ulaşıldı");
        }

        Gorev gorev = Gorev.builder()
            .baslik(requestDTO.getBaslik())
            .aciklama(requestDTO.getAciklama())
            .puanDegeri(requestDTO.getPuanDegeri())
            .sonTarih(requestDTO.getSonTarih())
            .kullanici(kullanici)
            .tamamlandi(false)
            .olusturmaTarihi(LocalDateTime.now())
            .durum("BEKLEMEDE")
            .gorevTipi(requestDTO.getGorevTipi())
            .build();
        
        // Kullanıcının şehirlerinden ilkini al ve göreve ata
        if (!kullanici.getCities().isEmpty()) {
            gorev.setSehir(kullanici.getCities().iterator().next());
        }

        Gorev kaydedilenGorev = gorevRepository.save(gorev);
        bildirimService.gorevOlusturulduBildirimiGonder(kullanici, kaydedilenGorev);
        return gorevToResponseDTO(kaydedilenGorev);
    }

    public GorevResponseDTO gorevTamamla(Long gorevId, String username) {
        Kullanici kullanici = kullaniciRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı", "kullanıcı adı", username));
            
        Gorev gorev = gorevRepository.findById(gorevId)
            .orElseThrow(() -> new ResourceNotFoundException("Görev", "id", gorevId));

        if (!gorev.getKullanici().equals(kullanici)) {
            throw new BusinessException("Bu görevi tamamlama yetkiniz yok");
        }

        if (gorev.isTamamlandi()) {
            throw new BusinessException("Bu görev zaten tamamlanmış");
        }

        // Günlük görev limitini kontrol et
        long gunlukGorevSayisi = gorevRepository.findByKullaniciIdAndTamamlandi(kullanici.getId(), true).size();
        if (gunlukGorevSayisi >= GUNLUK_GOREV_LIMITI) {
            throw new BusinessException("Günlük görev limitine ulaşıldı");
        }

        gorev.setTamamlandi(true);
        gorev.setTamamlanmaTarihi(LocalDateTime.now());
        
        // Kullanıcı puanını güncelle
        int yeniPuan = kullanici.getPoints() + GOREV_PUAN_DEGERI;
        kullanici.setPoints(yeniPuan);
        kullanici.setCompletedTaskCount(kullanici.getCompletedTaskCount() + 1);
        kullanici = kullaniciRepository.save(kullanici);
        
        // Şehir puanını güncelle
        if (gorev.getSehir() != null) {
            Sehir sehir = gorev.getSehir();
            sehir.setPoints(sehir.getPoints() + GOREV_PUAN_DEGERI);
            sehirRepository.save(sehir);
        }
        
        // Bina durumunu kontrol et ve güncelle
        if (gorev.getBuilding() != null) {
            Building bina = gorev.getBuilding();
            int tamamlananGorevSayisi = bina.getGunlukTamamlananGorevSayisi() + 1;
            bina.setGunlukTamamlananGorevSayisi(tamamlananGorevSayisi);
            
            // Bina tamamlanma kontrolü
            if (tamamlananGorevSayisi >= 10) {
                bina.setTamamlandi(true);
                bina.setTamamlanmaTarihi(LocalDateTime.now());
                bina.setHasRoof(true);
            } else {
                // Kat sayısını güncelle (her 10 görevde 1 kat)
                int katSayisi = tamamlananGorevSayisi / 10;
                if (katSayisi > MAKSIMUM_KAT_SAYISI) {
                    katSayisi = MAKSIMUM_KAT_SAYISI;
                }
                bina.setKatSayisi(katSayisi);
                
                // 10. katta çatı ekle
                if (katSayisi == MAKSIMUM_KAT_SAYISI) {
                    bina.setHasRoof(true);
                }
            }
            
            buildingRepository.save(bina);
        }
        
        bildirimService.gorevTamamlandiBildirimiGonder(kullanici, gorev);
        rozetService.gorevTamamlandiKontrol(kullanici);

        return gorevToResponseDTO(gorevRepository.save(gorev));
    }

    public List<GorevResponseDTO> gorevleriListele(String username) {
        Kullanici kullanici = kullaniciRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı", "kullanıcı adı", username));
            
        return gorevRepository.findByKullaniciId(kullanici.getId()).stream()
                .map(this::gorevToResponseDTO)
                .collect(Collectors.toList());
    }

    public GorevResponseDTO gorevDetay(Long id, String username) {
        Kullanici kullanici = kullaniciRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı", "kullanıcı adı", username));
            
        Gorev gorev = gorevRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Görev", "id", id));
            
        if (!gorev.getKullanici().equals(kullanici)) {
            throw new BusinessException("Bu göreve erişim yetkiniz yok");
        }
        
        return gorevToResponseDTO(gorev);
    }

    public GorevResponseDTO gorevGuncelle(Long id, GorevRequestDTO requestDTO, String username) {
        Kullanici kullanici = kullaniciRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı", "kullanıcı adı", username));
            
        Gorev gorev = gorevRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Görev", "id", id));
            
        if (!gorev.getKullanici().equals(kullanici)) {
            throw new BusinessException("Bu görevi güncelleme yetkiniz yok");
        }

        if (requestDTO.getBaslik() == null || requestDTO.getBaslik().trim().isEmpty()) {
            throw new BusinessException("Görev başlığı boş olamaz");
        }

        if (requestDTO.getPuanDegeri() <= 0) {
            throw new BusinessException("Görev puan değeri pozitif olmalıdır");
        }

        gorev.setBaslik(requestDTO.getBaslik());
        gorev.setAciklama(requestDTO.getAciklama());
        gorev.setPuanDegeri(requestDTO.getPuanDegeri());
        gorev.setSonTarih(requestDTO.getSonTarih());
        gorev.setGorevTipi(requestDTO.getGorevTipi());
        
        return gorevToResponseDTO(gorevRepository.save(gorev));
    }

    public void gorevSil(Long id, String username) {
        Kullanici kullanici = kullaniciRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı", "kullanıcı adı", username));
            
        Gorev gorev = gorevRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Görev", "id", id));
            
        if (!gorev.getKullanici().equals(kullanici)) {
            throw new BusinessException("Bu görevi silme yetkiniz yok");
        }
        
        gorevRepository.delete(gorev);
    }

    private GorevResponseDTO gorevToResponseDTO(Gorev gorev) {
        return GorevResponseDTO.builder()
                .id(gorev.getId())
                .baslik(gorev.getBaslik())
                .aciklama(gorev.getAciklama())
                .puanDegeri(gorev.getPuanDegeri())
                .tamamlandi(gorev.isTamamlandi())
                .kullaniciId(gorev.getKullanici().getId())
                .username(gorev.getKullanici().getUsername())
                .olusturmaTarihi(gorev.getOlusturmaTarihi())
                .tamamlanmaTarihi(gorev.getTamamlanmaTarihi())
                .sonTarih(gorev.getSonTarih())
                .gorevTipi(gorev.getGorevTipi())
                .build();
    }
} 