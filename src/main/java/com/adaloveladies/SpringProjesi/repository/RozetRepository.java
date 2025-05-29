package com.adaloveladies.SpringProjesi.repository;

import com.adaloveladies.SpringProjesi.model.Rozet;
import com.adaloveladies.SpringProjesi.model.Rozet.RozetTipi;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RozetRepository extends JpaRepository<Rozet, Long> {
    
    List<Rozet> findByKullanici(Kullanici kullanici);
    
    List<Rozet> findByKullaniciAndTip(Kullanici kullanici, RozetTipi tip);
    
    @Query("SELECT COUNT(r) > 0 FROM Rozet r WHERE r.kullanici = ?1 AND r.tip = ?2")
    boolean existsByKullaniciAndTip(Kullanici kullanici, RozetTipi tip);
    
    long countByKullanici(Kullanici kullanici);
    
    long countByKullaniciAndTip(Kullanici kullanici, RozetTipi tip);
} 