package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.dto.KullaniciRequestDTO;
import com.adaloveladies.SpringProjesi.dto.KullaniciResponseDTO;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.repository.KullaniciRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class KullaniciService {

    private final KullaniciRepository kullaniciRepository;
    private final PasswordEncoder passwordEncoder;
    private final SehirService sehirService;
    
    private static final int SEVIYE_ATLAMA_PUANI = 100;

    public KullaniciResponseDTO kullaniciOlustur(KullaniciRequestDTO requestDTO) {
        if (kullaniciRepository.findByKullaniciAdiOrEmail(requestDTO.getKullaniciAdi(), requestDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Kullanıcı adı veya email zaten kullanımda");
        }

        Kullanici kullanici = Kullanici.builder()
                .kullaniciAdi(requestDTO.getKullaniciAdi())
                .email(requestDTO.getEmail())
                .password(passwordEncoder.encode(requestDTO.getSifre()))
                .puan(0)
                .seviye(0)
                .olusturmaTarihi(LocalDateTime.now())
                .build();

        kullanici = kullaniciRepository.save(kullanici);
        
        // Kullanıcı için şehir oluştur
        sehirService.sehirOlustur(kullanici.getId());

        return kullaniciToResponseDTO(kullanici);
    }

    public KullaniciResponseDTO kullaniciGuncelle(Long id, KullaniciRequestDTO requestDTO) {
        Kullanici kullanici = findById(id);

        kullanici.setKullaniciAdi(requestDTO.getKullaniciAdi());
        kullanici.setEmail(requestDTO.getEmail());
        if (requestDTO.getSifre() != null && !requestDTO.getSifre().isEmpty()) {
            kullanici.setPassword(passwordEncoder.encode(requestDTO.getSifre()));
        }

        return kullaniciToResponseDTO(kullaniciRepository.save(kullanici));
    }

    public void kullaniciSil(Long id) {
        kullaniciRepository.deleteById(id);
    }

    public KullaniciResponseDTO kullaniciGetir(Long id) {
        return kullaniciToResponseDTO(findById(id));
    }

    public List<KullaniciResponseDTO> tumKullanicilariGetir() {
        return kullaniciRepository.findAll().stream()
                .map(this::kullaniciToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<KullaniciResponseDTO> enIyiKullanicilariGetir() {
        return kullaniciRepository.findTopKullanicilar().stream()
                .map(this::kullaniciToResponseDTO)
                .collect(Collectors.toList());
    }

    public void puanEkle(Long kullaniciId, Integer puan) {
        Kullanici kullanici = findById(kullaniciId);
        kullanici.setPuan(kullanici.getPuan() + puan);
        
        // Seviye kontrolü - Her seviye için gereken toplam puan: seviye * 100
        int yeniSeviye = kullanici.getPuan() / SEVIYE_ATLAMA_PUANI;
        if (yeniSeviye > kullanici.getSeviye()) {
            kullanici.setSeviye(yeniSeviye);
        }
        
        kullaniciRepository.save(kullanici);
        
        // Şehir puanını güncelle
        sehirService.puanEkle(kullaniciId, puan);
    }

    public Kullanici findById(Long id) {
        return kullaniciRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
    }

    private KullaniciResponseDTO kullaniciToResponseDTO(Kullanici kullanici) {
        return KullaniciResponseDTO.builder()
                .id(kullanici.getId())
                .kullaniciAdi(kullanici.getKullaniciAdi())
                .email(kullanici.getEmail())
                .puan(kullanici.getPuan())
                .seviye(kullanici.getSeviye())
                .olusturmaTarihi(kullanici.getOlusturmaTarihi())
                .build();
    }
} 