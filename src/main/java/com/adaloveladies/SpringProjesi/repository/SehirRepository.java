package com.adaloveladies.SpringProjesi.repository;

import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.model.Sehir;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SehirRepository extends JpaRepository<Sehir, Long> {
    Optional<Sehir> findByKullanici(Kullanici kullanici);
    Optional<Sehir> findByKullaniciId(Long kullaniciId);
} 