package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.dto.KullaniciRequestDTO;
import com.adaloveladies.SpringProjesi.dto.KullaniciResponseDTO;
import com.adaloveladies.SpringProjesi.exception.BusinessException;
import com.adaloveladies.SpringProjesi.exception.ResourceNotFoundException;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.model.Rol;
import com.adaloveladies.SpringProjesi.repository.KullaniciRepository;
import com.adaloveladies.SpringProjesi.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class KullaniciService {

    private final KullaniciRepository kullaniciRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final SehirService sehirService;
    
    private static final int SEVIYE_ATLAMA_PUANI = 100;

    public KullaniciResponseDTO createKullanici(KullaniciRequestDTO request) {
        if (kullaniciRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Bu kullanıcı adı zaten kullanımda");
        }
        if (kullaniciRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Bu email adresi zaten kullanımda");
        }

        Kullanici kullanici = Kullanici.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .points(0)
                .level(1)
                .completedTaskCount(0)
                .creationDate(LocalDateTime.now())
                .active(true)
                .build();

        Rol userRole = rolRepository.findByAd("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Varsayılan rol bulunamadı"));
        kullanici.getRoller().add(userRole);

        kullanici = kullaniciRepository.save(kullanici);
        
        // Kullanıcı için şehir oluştur
        sehirService.sehirOlustur(kullanici);

        return mapToResponseDTO(kullanici);
    }

    public KullaniciResponseDTO getKullaniciById(Long id) {
        Kullanici kullanici = kullaniciRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı: " + id));
        return mapToResponseDTO(kullanici);
    }

    public KullaniciResponseDTO updateKullanici(Long id, KullaniciRequestDTO request) {
        Kullanici kullanici = kullaniciRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı: " + id));

        if (!kullanici.getUsername().equals(request.getUsername()) &&
                kullaniciRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Bu kullanıcı adı zaten kullanımda");
        }
        if (!kullanici.getEmail().equals(request.getEmail()) &&
                kullaniciRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Bu email adresi zaten kullanımda");
        }

        kullanici.setUsername(request.getUsername());
        kullanici.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            kullanici.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        kullanici = kullaniciRepository.save(kullanici);
        return mapToResponseDTO(kullanici);
    }

    public void deleteKullanici(Long id) {
        Kullanici kullanici = kullaniciRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı: " + id));
        kullaniciRepository.delete(kullanici);
    }

    public List<KullaniciResponseDTO> getAllKullanicilar() {
        return kullaniciRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public void addPuan(Long id, Integer puan) {
        if (puan <= 0) {
            throw new BusinessException("Puan değeri pozitif olmalıdır");
        }

        Kullanici kullanici = kullaniciRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı: " + id));

        kullanici.setPoints(kullanici.getPoints() + puan);
        
        // Seviye kontrolü - Her seviye için gereken toplam puan: seviye * 100
        int yeniSeviye = kullanici.getPoints() / SEVIYE_ATLAMA_PUANI;
        if (yeniSeviye > kullanici.getLevel()) {
            kullanici.setLevel(yeniSeviye);
        }
        
        kullaniciRepository.save(kullanici);
        
        // Şehir puanını güncelle
        sehirService.puanEkle(kullanici, puan);
    }

    public List<KullaniciResponseDTO> enIyiKullanicilariGetir() {
        return kullaniciRepository.findTopKullanicilar().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public Kullanici findById(Long id) {
        return kullaniciRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı", "id", id));
    }

    public Kullanici findByKullaniciAdi(String kullaniciAdi) {
        return kullaniciRepository.findByUsername(kullaniciAdi)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı", "kullanıcı adı", kullaniciAdi));
    }

    public Optional<Kullanici> kullaniciGetir(Long id) {
        return kullaniciRepository.findById(id);
    }

    public Kullanici kullaniciKaydet(KullaniciRequestDTO request) {
        if (kullaniciRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Bu kullanıcı adı zaten kullanılıyor");
        }
        if (kullaniciRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Bu email zaten kullanılıyor");
        }

        Kullanici kullanici = Kullanici.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .points(0)
                .level(1)
                .completedTaskCount(0)
                .creationDate(LocalDateTime.now())
                .active(true)
                .build();

        Rol userRole = rolRepository.findByAd("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Varsayılan rol bulunamadı"));
        kullanici.getRoller().add(userRole);

        return kullaniciRepository.save(kullanici);
    }

    public Kullanici kullaniciGuncelle(Long id, KullaniciRequestDTO request) {
        Kullanici kullanici = kullaniciRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        if (!kullanici.getUsername().equals(request.getUsername()) &&
                kullaniciRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Bu kullanıcı adı zaten kullanılıyor");
        }
        if (!kullanici.getEmail().equals(request.getEmail()) &&
                kullaniciRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Bu email zaten kullanılıyor");
        }

        kullanici.setUsername(request.getUsername());
        kullanici.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            kullanici.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return kullaniciRepository.save(kullanici);
    }

    public void kullaniciSil(Long id) {
        if (!kullaniciRepository.existsById(id)) {
            throw new RuntimeException("Kullanıcı bulunamadı");
        }
        kullaniciRepository.deleteById(id);
    }

    public KullaniciResponseDTO kullaniciBilgileriniGetir(Long id) {
        Kullanici kullanici = kullaniciRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // Roller setini manuel olarak oluştur
        Set<String> rolAdlari = kullanici.getRoller().stream()
                .map(Rol::getName)
                .collect(Collectors.toSet());

        return KullaniciResponseDTO.builder()
                .id(kullanici.getId())
                .username(kullanici.getUsername())
                .email(kullanici.getEmail())
                .points(kullanici.getPoints())
                .level(kullanici.getLevel())
                .completedTaskCount(kullanici.getCompletedTaskCount())
                .creationDate(kullanici.getCreationDate())
                .active(kullanici.isActive())
                .roles(rolAdlari)
                .build();
    }

    private KullaniciResponseDTO mapToResponseDTO(Kullanici kullanici) {
        // Roller setini manuel olarak oluştur
        Set<String> rolAdlari = kullanici.getRoller().stream()
                .map(Rol::getName)
                .collect(Collectors.toSet());

        return KullaniciResponseDTO.builder()
                .id(kullanici.getId())
                .username(kullanici.getUsername())
                .email(kullanici.getEmail())
                .points(kullanici.getPoints())
                .level(kullanici.getLevel())
                .completedTaskCount(kullanici.getCompletedTaskCount())
                .creationDate(kullanici.getCreationDate())
                .active(kullanici.isActive())
                .roles(rolAdlari)
                .build();
    }
} 