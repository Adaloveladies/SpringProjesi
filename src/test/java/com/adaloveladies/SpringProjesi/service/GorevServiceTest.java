package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.config.TestConfig;
import com.adaloveladies.SpringProjesi.dto.GorevRequestDTO;
import com.adaloveladies.SpringProjesi.dto.GorevResponseDTO;
import com.adaloveladies.SpringProjesi.exception.BusinessException;
import com.adaloveladies.SpringProjesi.exception.ResourceNotFoundException;
import com.adaloveladies.SpringProjesi.model.Gorev;
import com.adaloveladies.SpringProjesi.model.GorevTipi;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.model.Sehir;
import com.adaloveladies.SpringProjesi.repository.GorevRepository;
import com.adaloveladies.SpringProjesi.repository.KullaniciRepository;
import com.adaloveladies.SpringProjesi.repository.SehirRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class GorevServiceTest {

    @Autowired
    private GorevService gorevService;

    @Autowired
    private GorevRepository gorevRepository;

    @Autowired
    private KullaniciRepository kullaniciRepository;

    @Autowired
    private SehirRepository sehirRepository;

    private Kullanici testKullanici;
    private GorevRequestDTO testGorevDTO;

    @BeforeEach
    void setUp() {
        gorevRepository.deleteAll();
        kullaniciRepository.deleteAll();
        sehirRepository.deleteAll();

        // Test kullanıcısını oluştur
        testKullanici = Kullanici.builder()
            .username("test_user")
            .email("test@example.com")
            .password("testpass")
            .points(0)
            .level(1)
            .completedTaskCount(0)
            .active(true)
            .creationDate(LocalDateTime.now())
            .build();
        testKullanici = kullaniciRepository.save(testKullanici);

        // Test şehrini oluştur
        Sehir testSehir = Sehir.builder()
            .name("Test Şehir")
            .description("Test Şehir Açıklaması")
            .active(true)
            .level(1)
            .points(0)
            .pointValue(10)
            .creationDate(LocalDateTime.now())
            .kullanici(testKullanici)
            .build();
        testSehir = sehirRepository.save(testSehir);

        // Kullanıcının şehirlerini güncelle
        testKullanici.getCities().add(testSehir);
        testKullanici = kullaniciRepository.save(testKullanici);

        // Test görev DTO'sunu oluştur
        testGorevDTO = new GorevRequestDTO();
        testGorevDTO.setBaslik("Test Görev");
        testGorevDTO.setAciklama("Test Görev Açıklaması");
        testGorevDTO.setPuanDegeri(10);
        testGorevDTO.setSonTarih(LocalDateTime.now().plusDays(1));
        testGorevDTO.setGorevTipi(GorevTipi.GUNLUK);
    }

    @Test
    void gorevOlustur_Basarili() {
        GorevResponseDTO response = gorevService.gorevOlustur(testGorevDTO, testKullanici.getUsername());

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(testGorevDTO.getBaslik(), response.getBaslik());
        assertEquals(testGorevDTO.getAciklama(), response.getAciklama());
        assertEquals(testKullanici.getId(), response.getKullaniciId());
        assertEquals(testKullanici.getUsername(), response.getUsername());
        assertFalse(response.isTamamlandi());
        assertNotNull(response.getOlusturmaTarihi());
        assertNull(response.getTamamlanmaTarihi());
    }

    @Test
    void gorevOlustur_KullaniciBulunamadi() {
        assertThrows(ResourceNotFoundException.class, () -> {
            gorevService.gorevOlustur(testGorevDTO, "olmayan_kullanici");
        });
    }

    @Test
    void gorevOlustur_GecersizVeri() {
        testGorevDTO.setBaslik(null);
        assertThrows(BusinessException.class, () -> {
            gorevService.gorevOlustur(testGorevDTO, testKullanici.getUsername());
        });

        testGorevDTO.setBaslik("Test Görev");
        testGorevDTO.setPuanDegeri(-1);
        assertThrows(BusinessException.class, () -> {
            gorevService.gorevOlustur(testGorevDTO, testKullanici.getUsername());
        });
    }

    @Test
    void gorevTamamla_Basarili() {
        GorevResponseDTO olusturulanGorev = gorevService.gorevOlustur(testGorevDTO, testKullanici.getUsername());
        
        GorevResponseDTO tamamlananGorev = gorevService.gorevTamamla(olusturulanGorev.getId(), testKullanici.getUsername());

        assertNotNull(tamamlananGorev);
        assertTrue(tamamlananGorev.isTamamlandi());
        assertNotNull(tamamlananGorev.getTamamlanmaTarihi());
        
        Kullanici guncelKullanici = kullaniciRepository.findById(testKullanici.getId()).orElseThrow();
        assertEquals(testGorevDTO.getPuanDegeri(), guncelKullanici.getPoints());
        assertEquals(1, guncelKullanici.getCompletedTaskCount());
        
        Sehir guncelSehir = sehirRepository.findById(testKullanici.getCities().iterator().next().getId()).orElseThrow();
        assertEquals(testGorevDTO.getPuanDegeri(), guncelSehir.getPoints());
    }

    @Test
    void gorevTamamla_GorevBulunamadi() {
        assertThrows(ResourceNotFoundException.class, () -> {
            gorevService.gorevTamamla(999L, testKullanici.getUsername());
        });
    }

    @Test
    void gorevTamamla_YetkisizKullanici() {
        GorevResponseDTO olusturulanGorev = gorevService.gorevOlustur(testGorevDTO, testKullanici.getUsername());
        
        final Kullanici baskaKullanici = new Kullanici();
        baskaKullanici.setUsername("baska_kullanici");
        baskaKullanici.setEmail("baska@example.com");
        baskaKullanici.setPassword("testpass");
        kullaniciRepository.save(baskaKullanici);

        assertThrows(BusinessException.class, () -> {
            gorevService.gorevTamamla(olusturulanGorev.getId(), baskaKullanici.getUsername());
        });
    }

    @Test
    void gorevTamamla_ZatenTamamlanmis() {
        GorevResponseDTO olusturulanGorev = gorevService.gorevOlustur(testGorevDTO, testKullanici.getUsername());
        gorevService.gorevTamamla(olusturulanGorev.getId(), testKullanici.getUsername());

        assertThrows(BusinessException.class, () -> {
            gorevService.gorevTamamla(olusturulanGorev.getId(), testKullanici.getUsername());
        });
    }

    @Test
    void gorevleriListele_Basarili() {
        gorevService.gorevOlustur(testGorevDTO, testKullanici.getUsername());
        gorevService.gorevOlustur(testGorevDTO, testKullanici.getUsername());

        List<GorevResponseDTO> gorevler = gorevService.gorevleriListele(testKullanici.getUsername());

        assertNotNull(gorevler);
        assertFalse(gorevler.isEmpty());
        assertEquals(2, gorevler.size());
        assertEquals(testGorevDTO.getBaslik(), gorevler.get(0).getBaslik());
    }

    @Test
    void gorevleriListele_KullaniciBulunamadi() {
        assertThrows(ResourceNotFoundException.class, () -> {
            gorevService.gorevleriListele("olmayan_kullanici");
        });
    }

    @Test
    void gorevDetay_Basarili() {
        GorevResponseDTO olusturulanGorev = gorevService.gorevOlustur(testGorevDTO, testKullanici.getUsername());
        
        GorevResponseDTO detay = gorevService.gorevDetay(olusturulanGorev.getId(), testKullanici.getUsername());

        assertNotNull(detay);
        assertEquals(olusturulanGorev.getId(), detay.getId());
        assertEquals(testGorevDTO.getBaslik(), detay.getBaslik());
        assertEquals(testGorevDTO.getAciklama(), detay.getAciklama());
        assertEquals(testGorevDTO.getPuanDegeri(), detay.getPuanDegeri());
        assertEquals(testGorevDTO.getSonTarih(), detay.getSonTarih());
    }

    @Test
    void gorevDetay_YetkisizKullanici() {
        GorevResponseDTO olusturulanGorev = gorevService.gorevOlustur(testGorevDTO, testKullanici.getUsername());
        
        final Kullanici baskaKullanici = new Kullanici();
        baskaKullanici.setUsername("baska_kullanici");
        baskaKullanici.setEmail("baska@example.com");
        baskaKullanici.setPassword("testpass");
        kullaniciRepository.save(baskaKullanici);

        assertThrows(BusinessException.class, () -> {
            gorevService.gorevDetay(olusturulanGorev.getId(), baskaKullanici.getUsername());
        });
    }

    @Test
    void gorevGuncelle_Basarili() {
        GorevResponseDTO olusturulanGorev = gorevService.gorevOlustur(testGorevDTO, testKullanici.getUsername());
        
        GorevRequestDTO guncelDTO = new GorevRequestDTO();
        guncelDTO.setBaslik("Güncellenmiş Görev");
        guncelDTO.setAciklama("Güncellenmiş Açıklama");
        guncelDTO.setPuanDegeri(20);
        guncelDTO.setSonTarih(LocalDateTime.now().plusDays(2));
        
        GorevResponseDTO guncellenenGorev = gorevService.gorevGuncelle(olusturulanGorev.getId(), guncelDTO, testKullanici.getUsername());

        assertNotNull(guncellenenGorev);
        assertEquals(guncelDTO.getBaslik(), guncellenenGorev.getBaslik());
        assertEquals(guncelDTO.getAciklama(), guncellenenGorev.getAciklama());
        assertEquals(guncelDTO.getPuanDegeri(), guncellenenGorev.getPuanDegeri());
        assertEquals(guncelDTO.getSonTarih(), guncellenenGorev.getSonTarih());
    }

    @Test
    void gorevGuncelle_GecersizVeri() {
        GorevResponseDTO olusturulanGorev = gorevService.gorevOlustur(testGorevDTO, testKullanici.getUsername());
        
        GorevRequestDTO guncelDTO = new GorevRequestDTO();
        guncelDTO.setBaslik(null);
        
        assertThrows(BusinessException.class, () -> {
            gorevService.gorevGuncelle(olusturulanGorev.getId(), guncelDTO, testKullanici.getUsername());
        });
    }

    @Test
    void gorevSil_Basarili() {
        GorevResponseDTO olusturulanGorev = gorevService.gorevOlustur(testGorevDTO, testKullanici.getUsername());
        
        gorevService.gorevSil(olusturulanGorev.getId(), testKullanici.getUsername());

        assertThrows(ResourceNotFoundException.class, () -> {
            gorevService.gorevDetay(olusturulanGorev.getId(), testKullanici.getUsername());
        });
    }

    @Test
    void gorevSil_YetkisizKullanici() {
        GorevResponseDTO olusturulanGorev = gorevService.gorevOlustur(testGorevDTO, testKullanici.getUsername());
        
        final Kullanici baskaKullanici = new Kullanici();
        baskaKullanici.setUsername("baska_kullanici");
        baskaKullanici.setEmail("baska@example.com");
        baskaKullanici.setPassword("testpass");
        kullaniciRepository.save(baskaKullanici);

        assertThrows(BusinessException.class, () -> {
            gorevService.gorevSil(olusturulanGorev.getId(), baskaKullanici.getUsername());
        });
    }
} 