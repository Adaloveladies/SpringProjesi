package com.adaloveladies.SpringProjesi.controller;

import com.adaloveladies.SpringProjesi.dto.TaskRequestDTO;
import com.adaloveladies.SpringProjesi.dto.TaskResponseDTO;
import com.adaloveladies.SpringProjesi.model.TaskStatus;
import com.adaloveladies.SpringProjesi.model.User;
import com.adaloveladies.SpringProjesi.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Görev işlemleri için REST API endpoint'lerini sağlayan controller
 */
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * Yeni görev oluşturur
     */
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(
            @Valid @RequestBody TaskRequestDTO taskDTO,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.createTask(taskDTO, user));
    }

    /**
     * Görevi günceller
     */
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable Long taskId,
            @Valid @RequestBody TaskRequestDTO taskDTO,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.updateTask(taskId, taskDTO, user));
    }

    /**
     * Görevi siler
     */
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long taskId,
            @AuthenticationPrincipal User user) {
        taskService.deleteTask(taskId, user);
        return ResponseEntity.ok().build();
    }

    /**
     * Kullanıcının tüm görevlerini getirir
     */
    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.getAllTasksByUser(user));
    }

    /**
     * Belirli durumdaki görevleri getirir
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByStatus(
            @PathVariable TaskStatus status,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.getTasksByStatus(user, status));
    }

    /**
     * Görev durumunu günceller
     */
    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskResponseDTO> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestParam TaskStatus newStatus,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.updateTaskStatus(taskId, newStatus, user));
    }

    /**
     * Görevlerde arama yapar
     */
    @GetMapping("/search")
    public ResponseEntity<List<TaskResponseDTO>> searchTasks(
            @RequestParam String query,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.searchTasks(user, query));
    }
} 