package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.dto.KullaniciRequestDTO;
import com.adaloveladies.SpringProjesi.dto.KullaniciResponseDTO;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.model.Rol;
import com.adaloveladies.SpringProjesi.repository.KullaniciRepository;
import com.adaloveladies.SpringProjesi.repository.RolRepository;
import com.adaloveladies.SpringProjesi.exception.ResourceNotFoundException;
import com.adaloveladies.SpringProjesi.exception.BusinessException;
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
    private RolRepository rolRepository;

    @InjectMocks
    private KullaniciService kullaniciService;

    private Kullanici testKullanici;
    private KullaniciRequestDTO testRequestDTO;
    private Rol userRol;

    @BeforeEach
    void setUp() {
        testKullanici = Kullanici.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("encodedPassword")
                .points(0)
                .level(1)
                .build();

        testRequestDTO = KullaniciRequestDTO.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .build();

        userRol = Rol.builder()
                .id(1L)
                .ad("ROLE_USER")
                .build();
    }

    @Test
    void createKullanici_Basarili() {
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(kullaniciRepository.save(any())).thenReturn(testKullanici);
        when(rolRepository.findByAd("ROLE_USER")).thenReturn(Optional.of(userRol));

        KullaniciResponseDTO response = kullaniciService.createKullanici(testRequestDTO);

        assertNotNull(response);
        assertEquals(testKullanici.getUsername(), response.getUsername());
        verify(kullaniciRepository).save(any());
    }

    @Test
    void getKullaniciById_Basarili() {
        when(kullaniciRepository.findById(1L)).thenReturn(Optional.of(testKullanici));

        KullaniciResponseDTO response = kullaniciService.getKullaniciById(1L);

        assertNotNull(response);
        assertEquals(testKullanici.getUsername(), response.getUsername());
    }

    @Test
    void getKullaniciById_KullaniciBulunamadi() {
        when(kullaniciRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> kullaniciService.getKullaniciById(1L));
    }

    @Test
    void updateKullanici_Basarili() {
        when(kullaniciRepository.findById(1L)).thenReturn(Optional.of(testKullanici));
        when(kullaniciRepository.save(any())).thenReturn(testKullanici);

        KullaniciResponseDTO response = kullaniciService.updateKullanici(1L, testRequestDTO);

        assertNotNull(response);
        assertEquals(testKullanici.getUsername(), response.getUsername());
    }

    @Test
    void updateKullanici_KullaniciBulunamadi() {
        when(kullaniciRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> kullaniciService.updateKullanici(1L, testRequestDTO));
    }

    @Test
    void deleteKullanici_Basarili() {
        when(kullaniciRepository.findById(1L)).thenReturn(Optional.of(testKullanici));
        doNothing().when(kullaniciRepository).deleteById(1L);

        kullaniciService.deleteKullanici(1L);

        verify(kullaniciRepository).deleteById(1L);
    }

    @Test
    void deleteKullanici_KullaniciBulunamadi() {
        when(kullaniciRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> kullaniciService.deleteKullanici(1L));
        verify(kullaniciRepository, never()).deleteById(any());
    }

    @Test
    void addPuan_Basarili() {
        when(kullaniciRepository.findById(1L)).thenReturn(Optional.of(testKullanici));
        when(kullaniciRepository.save(any())).thenReturn(testKullanici);

        kullaniciService.addPuan(1L, 100);

        assertEquals(100, testKullanici.getPoints());
        verify(kullaniciRepository).save(any());
    }

    @Test
    void addPuan_NegatifPuan() {
        assertThrows(BusinessException.class, () -> kullaniciService.addPuan(1L, -100));
    }
} 