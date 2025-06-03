package com.adaloveladies.SpringProjesi.controller;

import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.model.Task;
import com.adaloveladies.SpringProjesi.model.GorevTipi;
import com.adaloveladies.SpringProjesi.model.TaskStatus;
import com.adaloveladies.SpringProjesi.repository.KullaniciRepository;
import com.adaloveladies.SpringProjesi.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@ActiveProfiles("test")
public class LeaderboardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private KullaniciRepository kullaniciRepository;

    @Autowired
    private TaskRepository taskRepository;

    private Kullanici testUser;

    @BeforeEach
    void setup() {
        taskRepository.deleteAll();
        kullaniciRepository.deleteAll();

        testUser = new Kullanici();
        testUser.setUsername("testuser_" + System.currentTimeMillis());
        testUser.setEmail("testuser_" + System.currentTimeMillis() + "@example.com");
        testUser.setPassword("testpass");
        testUser.setPoints(0);
        testUser.setCompletedTaskCount(0);
        testUser.setActive(true);
        testUser = kullaniciRepository.save(testUser);
        kullaniciRepository.flush();

        Task task = new Task();
        task.setBaslik("Sample Task");
        task.setAciklama("Açıklama");
        task.setTamamlandi(true);
        task.setKullanici(testUser);
        task.setGorevTipi(GorevTipi.GUNLUK);
        task.setPuanDegeri(10);
        task.setOlusturmaTarihi(java.time.LocalDateTime.now());
        task.setDurum(TaskStatus.TAMAMLANDI);
        taskRepository.save(task);

        testUser.setPoints(10);
        testUser.setCompletedTaskCount(1);
        testUser = kullaniciRepository.save(testUser);
        kullaniciRepository.flush();

        testUser = kullaniciRepository.findById(testUser.getId()).orElseThrow();
    }

    @Test
    void getGlobalLeaderboard_Success() throws Exception {
        mockMvc.perform(get("/api/leaderboard/global")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].username").value(testUser.getUsername()))
                .andExpect(jsonPath("$[0].points").value(10))
                .andExpect(jsonPath("$[0].rank").value(1));
    }

    @Test
    public void getTopBuilders_Success() throws Exception {
        Task task = new Task();
        task.setBaslik("Sample Task");
        task.setAciklama("Açıklama");
        task.setTamamlandi(true);
        task.setKullanici(testUser);
        task.setGorevTipi(GorevTipi.GUNLUK);
        task.setPuanDegeri(10);
        task.setOlusturmaTarihi(java.time.LocalDateTime.now());
        task.setDurum(TaskStatus.TAMAMLANDI);
        taskRepository.save(task);

        testUser.setCompletedTaskCount(1);
        testUser = kullaniciRepository.save(testUser);
        kullaniciRepository.flush();

        // Kullanıcıyı tekrar yükleyerek completedTaskCount'un doğru şekilde kaydedildiğinden emin olalım
        testUser = kullaniciRepository.findById(testUser.getId()).orElseThrow();

        mockMvc.perform(get("/api/leaderboard/builders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].username").value(testUser.getUsername()))
                .andExpect(jsonPath("$[0].completedTaskCount").value(1));
    }

    @Test
    void getUserRanking_Success() throws Exception {
        mockMvc.perform(get("/api/leaderboard/user/{id}", testUser.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(testUser.getUsername()))
                .andExpect(jsonPath("$.points").value(10))
                .andExpect(jsonPath("$.rank").value(1));
    }
}
