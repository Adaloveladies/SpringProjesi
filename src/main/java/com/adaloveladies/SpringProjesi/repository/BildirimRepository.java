package com.adaloveladies.SpringProjesi.repository;

import com.adaloveladies.SpringProjesi.model.Bildirim;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BildirimRepository extends JpaRepository<Bildirim, Long> {
    List<Bildirim> findByKullaniciOrderByOlusturmaTarihiDesc(Kullanici kullanici);
    List<Bildirim> findByKullaniciAndOkunduFalse(Kullanici kullanici);
    long countByKullaniciAndOkunduFalse(Kullanici kullanici);
} 