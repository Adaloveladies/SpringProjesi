package com.adaloveladies.SpringProjesi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "buildings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Building {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer requiredLevel;

    @Column(nullable = false)
    private Integer floorCount;

    @Column(nullable = false)
    private Integer dailyCompletedTasks;

    @Column(nullable = false)
    private Boolean isCompleted;

    @Column(nullable = false)
    private Boolean hasRoof;

    @Column
    private LocalDateTime completedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Integer getRequiredLevel() {
        return requiredLevel;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
} 