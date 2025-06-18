package com.adaloveladies.SpringProjesi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
@Entity
@Table(name = "kullanicilar")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Kullanici {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Builder.Default
    private Integer points = 0;
    
    @Builder.Default
    private Integer level = 1;
    
    @Builder.Default
    private Integer completedTaskCount = 0;

    @Column(name = "creation_date")
    @Builder.Default
    private LocalDateTime creationDate = LocalDateTime.now();

    @Builder.Default
    private boolean active = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "kullanici_roller",
        joinColumns = @JoinColumn(name = "kullanici_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    @JsonIgnore
    private Set<Rol> roller = new HashSet<>();

    @OneToMany(mappedBy = "kullanici", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(mappedBy = "kullanici", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Sehir> cities = new HashSet<>();

    @OneToMany(mappedBy = "kullanici", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Bildirim> notifications = new HashSet<>();

    @OneToMany(mappedBy = "kullanici", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Rozet> rozetler = new HashSet<>();

    @OneToOne(mappedBy = "kullanici", cascade = CascadeType.ALL)
    @JsonIgnore
    private Istatistik statistics;

    @PrePersist
    protected void onCreate() {
        if (creationDate == null) {
            creationDate = LocalDateTime.now();
        }
        if (!active) {
            active = true;
        }
        if (points == null) {
            points = 0;
        }
        if (level == null) {
            level = 1;
        }
        if (completedTaskCount == null) {
            completedTaskCount = 0;
        }
    }

    // Business logic methods
    public void addPoints(int points) {
        this.points += points;
        checkLevel();
    }

    private void checkLevel() {
        int newLevel = (points / 1000) + 1;
        if (newLevel > level) {
            level = newLevel;
        }
    }

    public void completeTask() {
        completedTaskCount++;
    }

    public boolean hasRole(String roleName) {
        return roller.stream().anyMatch(rol -> rol.getName().equals(roleName));
    }

    public boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    public boolean isModerator() {
        return hasRole("ROLE_MODERATOR");
    }

    public boolean isUser() {
        return hasRole("ROLE_USER");
    }

    @JsonIgnore
    public Set<Rol> getRoller() {
        return roller;
    }

    public void setRoller(Set<Rol> roller) {
        this.roller = roller;
    }
} 