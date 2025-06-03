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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class KullaniciServiceTest {

    @Mock
    private KullaniciRepository kullaniciRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private SehirService sehirService;

    @InjectMocks
    private KullaniciService kullaniciService;

    private Kullanici testKullanici;
    private KullaniciRequestDTO testRequestDTO;
    private Set<Rol> testRoller;

    @BeforeEach
    void setUp() {
        testKullanici = new Kullanici();
        testKullanici.setId(1L);
        testKullanici.setUsername("testuser");
        testKullanici.setEmail("test@example.com");
        testKullanici.setPassword("password");
        testKullanici.setActive(true);
        testKullanici.setPoints(100);
        testKullanici.setLevel(1);
        testKullanici.setCompletedTaskCount(0);

        testRequestDTO = KullaniciRequestDTO.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .build();

        testRoller = new HashSet<>();
        Rol userRole = new Rol();
        userRole.setAd("ROLE_USER");
        testRoller.add(userRole);
        testKullanici.setRoller(testRoller);
    }

    @Test
    void createKullanici_Basarili() {
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(kullaniciRepository.save(any())).thenReturn(testKullanici);
        when(rolRepository.findByAd("ROLE_USER")).thenReturn(Optional.of(testRoller.iterator().next()));
        when(sehirService.sehirOlustur(any())).thenReturn(null);

        KullaniciResponseDTO response = kullaniciService.createKullanici(testRequestDTO);

        assertNotNull(response);
        assertEquals(testKullanici.getUsername(), response.getUsername());
        assertEquals(testKullanici.getEmail(), response.getEmail());
        verify(kullaniciRepository).save(any());
        verify(sehirService).sehirOlustur(any());
    }

    @Test
    void getKullaniciById_Basarili() {
        when(kullaniciRepository.findById(1L)).thenReturn(Optional.of(testKullanici));

        KullaniciResponseDTO response = kullaniciService.getKullaniciById(1L);

        assertNotNull(response);
        assertEquals(testKullanici.getUsername(), response.getUsername());
        assertEquals(testKullanici.getEmail(), response.getEmail());
        assertEquals(testKullanici.getPoints(), response.getPoints());
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
        assertEquals(testKullanici.getEmail(), response.getEmail());
    }

    @Test
    void updateKullanici_KullaniciBulunamadi() {
        when(kullaniciRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> kullaniciService.updateKullanici(1L, testRequestDTO));
    }

    @Test
    void deleteKullanici_Basarili() {
        when(kullaniciRepository.findById(1L)).thenReturn(Optional.of(testKullanici));
        doNothing().when(kullaniciRepository).delete(testKullanici);

        kullaniciService.deleteKullanici(1L);

        verify(kullaniciRepository).delete(testKullanici);
    }

    @Test
    void deleteKullanici_KullaniciBulunamadi() {
        when(kullaniciRepository.findById(99L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> kullaniciService.deleteKullanici(99L));
        verify(kullaniciRepository, never()).delete(any());
    }

    @Test
    void addPuan_Basarili() {
        when(kullaniciRepository.findById(1L)).thenReturn(Optional.of(testKullanici));
        when(kullaniciRepository.save(any())).thenReturn(testKullanici);
        doNothing().when(sehirService).puanEkle(any(), anyInt());

        kullaniciService.addPuan(1L, 50);

        assertEquals(150, testKullanici.getPoints());
        verify(sehirService).puanEkle(any(), anyInt());
    }

    @Test
    void addPuan_NegatifPuan() {
        assertThrows(BusinessException.class, () -> kullaniciService.addPuan(1L, -100));
    }
} 