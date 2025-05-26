package com.adaloveladies.SpringProjesi.controller;

import com.adaloveladies.SpringProjesi.model.User;
import com.adaloveladies.SpringProjesi.repository.BuildingRepository;
import com.adaloveladies.SpringProjesi.repository.TaskRepository;
import com.adaloveladies.SpringProjesi.repository.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        // Test verilerini temizle
        taskRepository.deleteAll();
        buildingRepository.deleteAll();
        userRepository.deleteAll();

        // Test kullanıcıları oluştur
        user1 = User.builder()
                .username("user1")
                .email("user1@example.com")
                .password("password")
                .score(150)
                .level(1)
                .build();

        user2 = User.builder()
                .username("user2")
                .email("user2@example.com")
                .password("password")
                .score(200)
                .level(2)
                .build();

        user3 = User.builder()
                .username("user3")
                .email("user3@example.com")
                .password("password")
                .score(80)
                .level(1)
                .build();

        userRepository.saveAll(java.util.Arrays.asList(user1, user2, user3));
    }

    @Test
    void getGlobalLeaderboard_Success() throws Exception {
        mockMvc.perform(get("/api/leaderboard/global"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user2")) // En yüksek puan
                .andExpect(jsonPath("$[1].username").value("user1"))
                .andExpect(jsonPath("$[2].username").value("user3"))
                .andExpect(jsonPath("$[0].rank").value(1))
                .andExpect(jsonPath("$[1].rank").value(2))
                .andExpect(jsonPath("$[2].rank").value(3));
    }

    @Test
    void getTopBuilders_Success() throws Exception {
        // Test için bina sayılarını ayarla
        user1.setScore(150);
        user2.setScore(200);
        user3.setScore(80);
        userRepository.saveAll(java.util.Arrays.asList(user1, user2, user3));

        mockMvc.perform(get("/api/leaderboard/builders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user2")) // En çok bina
                .andExpect(jsonPath("$[1].username").value("user1"))
                .andExpect(jsonPath("$[2].username").value("user3"))
                .andExpect(jsonPath("$[0].rank").value(1))
                .andExpect(jsonPath("$[1].rank").value(2))
                .andExpect(jsonPath("$[2].rank").value(3));
    }

    @Test
    void getUserRanking_Success() throws Exception {
        mockMvc.perform(get("/api/leaderboard/user/{userId}", user1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.totalScore").value(150))
                .andExpect(jsonPath("$.rank").value(2)); // user2'den sonra 2. sırada
    }

    @Test
    void getUserRanking_UserNotFound() throws Exception {
        mockMvc.perform(get("/api/leaderboard/user/999"))
                .andExpect(status().isNotFound());
    }
} 