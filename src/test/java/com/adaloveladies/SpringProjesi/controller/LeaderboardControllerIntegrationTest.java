package com.adaloveladies.SpringProjesi.controller;

import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.repository.BuildingRepository;
import com.adaloveladies.SpringProjesi.repository.TaskRepository;
import com.adaloveladies.SpringProjesi.repository.KullaniciRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LeaderboardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private KullaniciRepository kullaniciRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    private Kullanici kullanici1;
    private Kullanici kullanici2;
    private Kullanici kullanici3;

    @BeforeEach
    void setUp() {
        // Test verilerini temizle
        taskRepository.deleteAll();
        buildingRepository.deleteAll();
        kullaniciRepository.deleteAll();
        kullaniciRepository.flush(); // Değişiklikleri hemen uygula

        // Test kullanıcıları oluştur
        kullanici1 = Kullanici.builder()
            .username("kullanici1")
            .email("test1@example.com") // Benzersiz e-posta
            .password("sifre")
            .points(150)
            .level(1)
            .build();

        kullanici2 = Kullanici.builder()
            .username("kullanici2")
            .email("test2@example.com") // Benzersiz e-posta
            .password("sifre")
            .points(200)
            .level(2)
            .build();

        kullanici3 = Kullanici.builder()
            .username("kullanici3")
            .email("test3@example.com") // Benzersiz e-posta
            .password("sifre")
            .points(80)
            .level(1)
            .build();

        kullaniciRepository.saveAll(java.util.Arrays.asList(kullanici1, kullanici2, kullanici3));
        kullaniciRepository.flush(); // Değişiklikleri hemen uygula
    }

    @Test
    void getGlobalLeaderboard_Success() throws Exception {
        mockMvc.perform(get("/api/leaderboard/global"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("kullanici2")) // En yüksek puan
                .andExpect(jsonPath("$[1].username").value("kullanici1"))
                .andExpect(jsonPath("$[2].username").value("kullanici3"))
                .andExpect(jsonPath("$[0].rank").value(1))
                .andExpect(jsonPath("$[1].rank").value(2))
                .andExpect(jsonPath("$[2].rank").value(3));
    }

    @Test
    void getTopBuilders_Success() throws Exception {
        // Test için bina sayılarını ayarla
        kullanici1.setPoints(150);
        kullanici2.setPoints(200);
        kullanici3.setPoints(80);
        kullaniciRepository.saveAll(java.util.Arrays.asList(kullanici1, kullanici2, kullanici3));

        mockMvc.perform(get("/api/leaderboard/builders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("kullanici2")) // En çok bina
                .andExpect(jsonPath("$[1].username").value("kullanici1"))
                .andExpect(jsonPath("$[2].username").value("kullanici3"))
                .andExpect(jsonPath("$[0].rank").value(1))
                .andExpect(jsonPath("$[1].rank").value(2))
                .andExpect(jsonPath("$[2].rank").value(3));
    }

    @Test
    void getUserRanking_Success() throws Exception {
        mockMvc.perform(get("/api/leaderboard/user/{userId}", kullanici1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("kullanici1"))
                .andExpect(jsonPath("$.points").value(150))
                .andExpect(jsonPath("$.rank").value(2)); // kullanici2'den sonra 2. sırada
    }

    @Test
    void getUserRanking_UserNotFound() throws Exception {
        mockMvc.perform(get("/api/leaderboard/user/999"))
                .andExpect(status().isNotFound());
    }
} 