package com.adaloveladies.SpringProjesi.controller;

import com.adaloveladies.SpringProjesi.dto.TaskRequestDTO;
import com.adaloveladies.SpringProjesi.dto.TaskResponseDTO;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.model.TaskStatus;
import com.adaloveladies.SpringProjesi.service.TaskService;
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

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks(@AuthenticationPrincipal Kullanici kullanici) {
        return ResponseEntity.ok(taskService.getAllTasks(kullanici));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long taskId, @AuthenticationPrincipal Kullanici kullanici) {
        return ResponseEntity.ok(taskService.getTaskById(taskId, kullanici));
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskRequestDTO taskRequest, @AuthenticationPrincipal Kullanici kullanici) {
        return ResponseEntity.ok(taskService.createTask(taskRequest, kullanici));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long taskId, @RequestBody TaskRequestDTO taskRequest, @AuthenticationPrincipal Kullanici kullanici) {
        return ResponseEntity.ok(taskService.updateTask(taskId, taskRequest, kullanici));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId, @AuthenticationPrincipal Kullanici kullanici) {
        taskService.deleteTask(taskId, kullanici);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{taskId}/status")
    public ResponseEntity<TaskResponseDTO> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestParam TaskStatus status,
            @AuthenticationPrincipal Kullanici kullanici) {
        return ResponseEntity.ok(taskService.updateTaskStatus(taskId, status, kullanici));
    }

    @GetMapping("/status/{durum}")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByStatus(@PathVariable TaskStatus durum, @AuthenticationPrincipal Kullanici kullanici) {
        return ResponseEntity.ok(taskService.getTasksByStatus(kullanici, durum));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TaskResponseDTO>> searchTasks(@RequestParam String searchTerm, @AuthenticationPrincipal Kullanici kullanici) {
        return ResponseEntity.ok(taskService.searchTasks(kullanici, searchTerm));
    }
} 