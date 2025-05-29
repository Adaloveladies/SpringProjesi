package com.adaloveladies.SpringProjesi.repository;

import com.adaloveladies.SpringProjesi.model.Istatistik;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IstatistikRepository extends JpaRepository<Istatistik, Long> {
    
    Optional<Istatistik> findByKullanici(Kullanici kullanici);
    
    @Query("SELECT AVG(i.basariOrani) FROM Istatistik i")
    double calculateAverageSuccessRate();
    
    @Query("SELECT COUNT(i) FROM Istatistik i WHERE i.basariOrani > ?1")
    long countByBasariOraniGreaterThan(double basariOrani);
    
    @Query("SELECT i FROM Istatistik i ORDER BY i.toplamGorevSayisi DESC")
    List<Istatistik> findTopByOrderByToplamGorevSayisiDesc(Pageable pageable);
    
    @Query("SELECT COUNT(g) FROM Gorev g WHERE g.tamamlanmaTarihi > ?1")
    long countByTamamlanmaTarihiAfter(LocalDateTime tarih);
    
    @Query("SELECT COUNT(k) FROM Kullanici k WHERE k.olusturmaTarihi > ?1")
    long countByKayitTarihiAfter(LocalDateTime tarih);
} 