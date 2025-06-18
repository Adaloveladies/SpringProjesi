package com.adaloveladies.SpringProjesi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roller")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String ad;

    private String aciklama;

    public String getName() {
        return ad;
    }

    public void setName(String name) {
        this.ad = name;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getDescription() {
        return aciklama;
    }

    public void setDescription(String description) {
        this.aciklama = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
} 