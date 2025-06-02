package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.dto.GorevRequestDTO;
import com.adaloveladies.SpringProjesi.dto.GorevResponseDTO;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.model.TaskStatus;
import com.adaloveladies.SpringProjesi.model.GorevTipi;
import com.adaloveladies.SpringProjesi.repository.KullaniciRepository;
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
class GorevServiceTest {

    @Autowired
    private GorevService gorevService;

    @Autowired
    private KullaniciRepository kullaniciRepository;

    private Kullanici testKullanici;
    private GorevRequestDTO testGorevRequest;
    private static final String TEST_USERNAME = "testuser";
    private static final int GUNLUK_GOREV_PUANI = 10;

    @BeforeEach
    void setUp() {
        // Test kullanıcısı oluştur
        testKullanici = Kullanici.builder()
            .username(TEST_USERNAME)
            .email("test@example.com")
            .password("password")
            .points(0)
            .level(1)
            .build();
        testKullanici = kullaniciRepository.save(testKullanici);

        // Test görevi için request DTO oluştur
        testGorevRequest = GorevRequestDTO.builder()
            .baslik("Test Görevi")
            .aciklama("Test görevi açıklaması")
            .sonTarih(LocalDateTime.now().plusDays(1))
            .gorevTipi(GorevTipi.GUNLUK)
            .build();
    }

    @Test
    void gorevOlustur_Basarili() {
        // when
        GorevResponseDTO response = gorevService.gorevOlustur(testGorevRequest, TEST_USERNAME);

        // then
        assertNotNull(response);
        assertEquals(testGorevRequest.getBaslik(), response.getBaslik());
        assertEquals(testGorevRequest.getAciklama(), response.getAciklama());
        assertEquals(GUNLUK_GOREV_PUANI, response.getPuanDegeri());
    }

    @Test
    void gorevTamamla_Basarili() {
        // given
        GorevResponseDTO gorev = gorevService.gorevOlustur(testGorevRequest, TEST_USERNAME);

        // when
        GorevResponseDTO tamamlananGorev = gorevService.gorevTamamla(gorev.getId(), TEST_USERNAME);

        // then
        assertNotNull(tamamlananGorev);
        assertEquals(TaskStatus.TAMAMLANDI, tamamlananGorev.getDurum());
        assertNotNull(tamamlananGorev.getTamamlanmaTarihi());
    }

    @Test
    void gorevleriListele_Basarili() {
        // given
        gorevService.gorevOlustur(testGorevRequest, TEST_USERNAME);
        gorevService.gorevOlustur(testGorevRequest, TEST_USERNAME);

        // when
        List<GorevResponseDTO> gorevler = gorevService.gorevleriListele(TEST_USERNAME);

        // then
        assertNotNull(gorevler);
        assertEquals(2, gorevler.size());
    }
} 