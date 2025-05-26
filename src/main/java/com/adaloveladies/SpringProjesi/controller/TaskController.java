package com.adaloveladies.SpringProjesi.controller;

import com.adaloveladies.SpringProjesi.dto.TaskRequestDTO;
import com.adaloveladies.SpringProjesi.exception.ResourceNotFoundException;
import com.adaloveladies.SpringProjesi.model.TaskStatus;
import com.adaloveladies.SpringProjesi.model.User;
import com.adaloveladies.SpringProjesi.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Görev işlemleri için REST API endpoint'lerini sağlayan controller
 */
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody TaskRequestDTO taskDTO, @AuthenticationPrincipal User user) {
        try {
            return ResponseEntity.ok(taskService.createTask(taskDTO, user));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody TaskRequestDTO taskDTO, @AuthenticationPrincipal User user) {
        try {
            return ResponseEntity.ok(taskService.updateTask(id, taskDTO, user));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id, @AuthenticationPrincipal User user) {
        try {
            taskService.deleteTask(id, user);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllTasks(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.getAllTasksByUser(user));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getTasksByStatus(@PathVariable TaskStatus status, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.getTasksByStatus(user, status));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateTaskStatus(@PathVariable Long id, @RequestParam TaskStatus status, @AuthenticationPrincipal User user) {
        try {
            return ResponseEntity.ok(taskService.updateTaskStatus(id, status, user));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchTasks(@RequestParam String query, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.searchTasks(user, query));
    }
} 