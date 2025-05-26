package com.adaloveladies.SpringProjesi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Görev modeli
 * Kullanıcıların oluşturduğu görevleri temsil eder
 */
@Entity
@Table(name = "tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // Görev başlığı

    @Column(nullable = false)
    private String description; // Görev açıklaması

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status; // Görev durumu

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Görevi oluşturan kullanıcı

    @Column(nullable = false)
    private LocalDateTime createdAt; // Oluşturulma tarihi

    @Column
    private LocalDateTime completedAt; // Tamamlanma tarihi

    /**
     * Görev oluşturulduğunda otomatik olarak tarih atar
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 