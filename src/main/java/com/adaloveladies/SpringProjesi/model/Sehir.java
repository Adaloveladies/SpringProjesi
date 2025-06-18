package com.adaloveladies.SpringProjesi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Şehir modeli
 * Kullanıcıların oluşturduğu şehirleri temsil eder
 */
@SuppressWarnings("unused")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "sehirler")
public class Sehir {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Integer points;

    @Column(nullable = false)
    private Integer level;

    @Column(name = "kullanici_id", insertable = false, updatable = false)
    private Long kullaniciId;

    @Column(nullable = false)
    private int pointValue;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kullanici_id", nullable = false)
    @JsonIgnore
    private Kullanici kullanici;

    @OneToMany(mappedBy = "sehir", cascade = CascadeType.ALL)
    @Builder.Default
    @JsonIgnore
    private Set<Building> buildings = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        creationDate = LocalDateTime.now();
        active = true;
        level = 1;
        pointValue = level * 100;
    }

    @PreUpdate
    protected void onUpdate() {
        // Bu metodun içeriği mevcut kodda bulunmamaktadır.
        // Eğer bu metodun içeriği güncellenecekse, buraya yeni işlem eklenebilir.
    }

    /**
     * Şehre puan ekler ve gerekirse seviye atlar
     * @param points Eklenecek puan miktarı
     */
    public void addPoints(int points) {
        this.pointValue += points;
        checkLevel();
        onUpdate();
    }

    private void checkLevel() {
        if (kullanici.getPoints() >= pointValue) {
            level++;
            pointValue = level * 100;
        }
    }

    public boolean canLevelUp() {
        return kullanici.getPoints() >= pointValue;
    }

    public void levelUp() {
        level++;
        pointValue = level * 100;
    }

    /**
     * Şehre yeni bina ekler
     */
    public void binaEkle() {
        // Bu metodun içeriği mevcut kodda bulunmamaktadır.
        // Eğer bu metodun içeriği güncellenecekse, buraya yeni işlem eklenebilir.
        onUpdate();
    }

    /**
     * Şehirden bina siler
     */
    public void binaSil() {
        // Bu metodun içeriği mevcut kodda bulunmamaktadır.
        // Eğer bu metodun içeriği güncellenecekse, buraya yeni işlem eklenebilir.
        onUpdate();
    }

    /**
     * Şehre yeni görev ekler
     */
    public void gorevEkle() {
        // Bu metodun içeriği mevcut kodda bulunmamaktadır.
        // Eğer bu metodun içeriği güncellenecekse, buraya yeni işlem eklenebilir.
        onUpdate();
    }

    /**
     * Şehirdeki görevi tamamlandı olarak işaretler
     */
    public void gorevTamamla() {
        // Bu metodun içeriği mevcut kodda bulunmamaktadır.
        // Eğer bu metodun içeriği güncellenecekse, buraya yeni işlem eklenebilir.
        onUpdate();
    }

    /**
     * Şehrin gelişmişlik oranını hesaplar
     * @return Şehrin gelişmişlik oranı (0-1 arası)
     */
    public double getSehirGelismislikOrani() {
        // Bu metodun içeriği mevcut kodda bulunmamaktadır.
        // Eğer bu metodun içeriği güncellenecekse, buraya yeni işlem eklenebilir.
        return 0;
    }

    /**
     * Şehrin başarı oranını hesaplar
     * @return Tamamlanan görevlerin toplam görevlere oranı (0-100 arası)
     */
    public double getBasariOrani() {
        // Bu metodun içeriği mevcut kodda bulunmamaktadır.
        // Eğer bu metodun içeriği güncellenecekse, buraya yeni işlem eklenebilir.
        return 0;
    }

    /**
     * Şehre rozet ekler
     */
    public void rozetEkle() {
        // Bu metodun içeriği mevcut kodda bulunmamaktadır.
        // Eğer bu metodun içeriği güncellenecekse, buraya yeni işlem eklenebilir.
        onUpdate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getKullaniciId() {
        return kullaniciId;
    }

    public void setKullaniciId(Long kullaniciId) {
        this.kullaniciId = kullaniciId;
    }

    public int getPointValue() {
        return pointValue;
    }

    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
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

    public Kullanici getKullanici() {
        return kullanici;
    }

    public void setKullanici(Kullanici kullanici) {
        this.kullanici = kullanici;
    }

    public Set<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(Set<Building> buildings) {
        this.buildings = buildings;
    }
} 