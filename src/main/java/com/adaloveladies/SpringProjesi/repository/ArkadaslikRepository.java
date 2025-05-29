package com.adaloveladies.SpringProjesi.repository;

import com.adaloveladies.SpringProjesi.model.Arkadaslik;
import com.adaloveladies.SpringProjesi.model.ArkadaslikDurumu;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ArkadaslikRepository extends JpaRepository<Arkadaslik, Long> {
    List<Arkadaslik> findByGonderenAndDurum(Kullanici gonderen, ArkadaslikDurumu durum);
    List<Arkadaslik> findByAlanAndDurum(Kullanici alan, ArkadaslikDurumu durum);
    
    @Query("SELECT a FROM Arkadaslik a WHERE (a.gonderen = :kullanici OR a.alan = :kullanici) AND a.durum = 'KABUL_EDILDI'")
    List<Arkadaslik> findArkadaslar(Kullanici kullanici);
    
    Optional<Arkadaslik> findByGonderenAndAlan(Kullanici gonderen, Kullanici alan);
    
    boolean existsByGonderenAndAlanAndDurum(Kullanici gonderen, Kullanici alan, ArkadaslikDurumu durum);
    
    @Query("SELECT COUNT(a) FROM Arkadaslik a WHERE (a.gonderen = :kullanici OR a.alan = :kullanici) AND a.durum = 'KABUL_EDILDI'")
    long countArkadasSayisi(Kullanici kullanici);
} 