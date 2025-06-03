package com.adaloveladies.SpringProjesi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Entity
@Table(name = "kullanicilar")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Kullanici implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private Integer points;
    private Integer level;
    private Integer completedTaskCount;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    private boolean active;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "kullanici_roller",
        joinColumns = @JoinColumn(name = "kullanici_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    @Builder.Default
    private Set<Rol> roller = new HashSet<>();

    @OneToMany(mappedBy = "kullanici", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(mappedBy = "kullanici", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Sehir> cities = new HashSet<>();

    @OneToMany(mappedBy = "kullanici", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Bildirim> notifications = new HashSet<>();

    @OneToMany(mappedBy = "kullanici", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Rozet> rozetler = new HashSet<>();

    @OneToOne(mappedBy = "kullanici", cascade = CascadeType.ALL)
    private Istatistik statistics;

    @PrePersist
    protected void onCreate() {
        creationDate = LocalDateTime.now();
        active = true;
        points = 0;
        level = 1;
        completedTaskCount = 0;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roller.stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getCompletedTaskCount() {
        return completedTaskCount;
    }

    public void setCompletedTaskCount(Integer completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

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

    public Set<String> getRolAdlari() {
        return roller.stream()
                .map(Rol::getName)
                .collect(Collectors.toSet());
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

    public Integer getSeviye() {
        return level;
    }

    public void setSeviye(Integer seviye) {
        this.level = seviye;
    }

    public String getKullaniciAdi() {
        return username;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        this.username = kullaniciAdi;
    }

    public Integer getTamamlananGorevSayisi() {
        return completedTaskCount;
    }

    public void setTamamlananGorevSayisi(Integer tamamlananGorevSayisi) {
        this.completedTaskCount = tamamlananGorevSayisi;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Rol> getRoller() {
        return roller;
    }

    public void setRoller(Set<Rol> roller) {
        this.roller = roller;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Set<Sehir> getCities() {
        return cities;
    }

    public void setCities(Set<Sehir> cities) {
        this.cities = cities;
    }

    public Set<Bildirim> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Bildirim> notifications) {
        this.notifications = notifications;
    }

    public Set<Rozet> getRozetler() {
        return rozetler;
    }

    public void setRozetler(Set<Rozet> rozetler) {
        this.rozetler = rozetler;
    }

    public Istatistik getStatistics() {
        return statistics;
    }

    public void setStatistics(Istatistik statistics) {
        this.statistics = statistics;
    }
} 