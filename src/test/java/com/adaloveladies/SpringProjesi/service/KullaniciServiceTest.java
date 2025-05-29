package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.dto.KullaniciRequestDTO;
import com.adaloveladies.SpringProjesi.dto.KullaniciResponseDTO;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.repository.KullaniciRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class KullaniciServiceTest {

    @Mock
    private KullaniciRepository kullaniciRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SehirService sehirService;

    @InjectMocks
    private KullaniciService kullaniciService;

    private Kullanici testKullanici;
    private KullaniciRequestDTO testRequestDTO;

    @BeforeEach
    void setUp() {
        testKullanici = new Kullanici();
        testKullanici.setId(1L);
        testKullanici.setKullaniciAdi("testuser");
        testKullanici.setEmail("test@example.com");
        testKullanici.setPassword("password");
        testKullanici.setPuan(0);
        testKullanici.setSeviye(0);

        testRequestDTO = new KullaniciRequestDTO();
        testRequestDTO.setKullaniciAdi("testuser");
        testRequestDTO.setEmail("test@example.com");
        testRequestDTO.setSifre("password");
    }

    @Test
    void kullaniciOlustur_Basarili() {
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(kullaniciRepository.save(any())).thenReturn(testKullanici);
        when(kullaniciRepository.findByKullaniciAdiOrEmail(any(), any())).thenReturn(Optional.empty());

        KullaniciResponseDTO response = kullaniciService.kullaniciOlustur(testRequestDTO);

        assertNotNull(response);
        assertEquals(testKullanici.getKullaniciAdi(), response.getKullaniciAdi());
        verify(kullaniciRepository).save(any());
        verify(sehirService).sehirOlustur(any());
    }

    @Test
    void kullaniciGetir_Basarili() {
        when(kullaniciRepository.findById(1L)).thenReturn(Optional.of(testKullanici));

        KullaniciResponseDTO response = kullaniciService.kullaniciGetir(1L);

        assertNotNull(response);
        assertEquals(testKullanici.getId(), response.getId());
    }

    @Test
    void kullaniciGetir_Bulunamadi() {
        when(kullaniciRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> kullaniciService.kullaniciGetir(1L));
    }

    @Test
    void kullaniciGuncelle_Basarili() {
        when(kullaniciRepository.findById(1L)).thenReturn(Optional.of(testKullanici));
        when(kullaniciRepository.save(any())).thenReturn(testKullanici);

        KullaniciResponseDTO response = kullaniciService.kullaniciGuncelle(1L, testRequestDTO);

        assertNotNull(response);
        verify(kullaniciRepository).save(any());
    }

    @Test
    void kullaniciSil_Basarili() {
        when(kullaniciRepository.findById(1L)).thenReturn(Optional.of(testKullanici));
        doNothing().when(kullaniciRepository).delete(any());

        kullaniciService.kullaniciSil(1L);

        verify(kullaniciRepository).delete(any());
    }

    @Test
    void puanEkle_Basarili() {
        when(kullaniciRepository.findById(1L)).thenReturn(Optional.of(testKullanici));
        when(kullaniciRepository.save(any())).thenReturn(testKullanici);

        kullaniciService.puanEkle(1L, 100);

        assertEquals(100, testKullanici.getPuan());
        verify(kullaniciRepository).save(any());
        verify(sehirService).puanEkle(any(), any());
    }

    @Test
    void puanEkle_NegatifPuan() {
        when(kullaniciRepository.findById(1L)).thenReturn(Optional.of(testKullanici));

        assertThrows(IllegalArgumentException.class, () -> kullaniciService.puanEkle(1L, -100));
    }
} 