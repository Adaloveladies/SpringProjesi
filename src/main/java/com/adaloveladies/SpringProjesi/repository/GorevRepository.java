package com.adaloveladies.SpringProjesi.repository;

import com.adaloveladies.SpringProjesi.model.Gorev;
import com.adaloveladies.SpringProjesi.model.GorevDurumu;
import com.adaloveladies.SpringProjesi.model.GorevTipi;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GorevRepository extends JpaRepository<Gorev, Long> {
    
    // Kullanıcının görevlerini getir
    List<Gorev> findByKullanici(Kullanici kullanici);
    
    // Kullanıcının belirli durumdaki görevlerini getir
    List<Gorev> findByKullaniciAndDurum(Kullanici kullanici, GorevDurumu durum);
    
    // Kullanıcının belirli tipteki görevlerini getir
    List<Gorev> findByKullaniciAndTip(Kullanici kullanici, GorevTipi tip);
    
    // Süresi dolmuş görevleri getir
    @Query("SELECT g FROM Gorev g WHERE g.sonTarih < :simdi AND g.durum = :durum")
    List<Gorev> findSuresiDolanGorevler(@Param("simdi") LocalDateTime simdi, @Param("durum") GorevDurumu durum);
    
    // Kullanıcının tamamladığı görevleri getir
    @Query("SELECT g FROM Gorev g WHERE g.kullanici = :kullanici AND g.durum = :durum")
    List<Gorev> findTamamlananGorevler(@Param("kullanici") Kullanici kullanici, @Param("durum") GorevDurumu durum);
    
    // Kullanıcının aktif görevlerini getir
    @Query("SELECT g FROM Gorev g WHERE g.kullanici = :kullanici AND g.durum = :durum")
    List<Gorev> findAktifGorevler(@Param("kullanici") Kullanici kullanici, @Param("durum") GorevDurumu durum);
    
    long countByKullanici(Kullanici kullanici);
    
    long countByKullaniciAndDurum(Kullanici kullanici, GorevDurumu durum);
    
    @Query("SELECT g FROM Gorev g WHERE g.kullanici = :kullanici AND g.durum = :durum AND g.tamamlanmaTarihi BETWEEN :baslangic AND :bitis")
    List<Gorev> findByKullaniciAndDurumAndTamamlanmaTarihiBetween(
        @Param("kullanici") Kullanici kullanici, 
        @Param("durum") GorevDurumu durum, 
        @Param("baslangic") LocalDateTime baslangic, 
        @Param("bitis") LocalDateTime bitis);
    
    long countByKullaniciAndDurumAndTamamlanmaTarihiBetween(
        Kullanici kullanici, GorevDurumu durum, LocalDateTime baslangic, LocalDateTime bitis);
    
    @Query("SELECT g FROM Gorev g WHERE g.kullanici = :kullanici AND g.durum = :durum AND g.sonTarih < :simdi")
    List<Gorev> findGecikenGorevler(
        @Param("kullanici") Kullanici kullanici, 
        @Param("durum") GorevDurumu durum, 
        @Param("simdi") LocalDateTime simdi);
    
    @Query("SELECT g FROM Gorev g WHERE g.kullanici = :kullanici AND g.durum = :durum AND g.sonTarih BETWEEN :baslangic AND :bitis")
    List<Gorev> findYaklasanGorevler(
        @Param("kullanici") Kullanici kullanici, 
        @Param("durum") GorevDurumu durum, 
        @Param("baslangic") LocalDateTime baslangic, 
        @Param("bitis") LocalDateTime bitis);
} 