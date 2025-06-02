package com.adaloveladies.SpringProjesi.repository;

import com.adaloveladies.SpringProjesi.model.Istatistik;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface IstatistikRepository extends JpaRepository<Istatistik, Long> {
    Optional<Istatistik> findByKullanici(Kullanici kullanici);
    
    long countByKayitTarihiAfter(LocalDateTime tarih);
    
    Page<Istatistik> findTopByOrderByTamamlananGorevSayisiDesc(Pageable pageable);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.kullanici = ?1 AND t.tamamlandi = true")
    int countTamamlananGorevByKullanici(Kullanici kullanici);

    @Query("SELECT COUNT(DISTINCT t.kullanici) FROM Task t")
    long countToplamKullanici();

    @Query("SELECT COUNT(t) FROM Task t")
    long countToplamGorev();

    @Query("SELECT COUNT(t) FROM Task t WHERE t.tamamlandi = true")
    long countTamamlananGorev();

    @Query("SELECT SUM(i.tamamlananGorevSayisi) FROM Istatistik i")
    Long sumTamamlananGorevSayisi();

    @Query("SELECT SUM(i.toplamPuan) FROM Istatistik i")
    Long sumToplamPuan();
} 