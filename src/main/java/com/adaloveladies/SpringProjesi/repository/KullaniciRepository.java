package com.adaloveladies.SpringProjesi.repository;

import com.adaloveladies.SpringProjesi.model.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KullaniciRepository extends JpaRepository<Kullanici, Long> {
    
    // Kullanıcı adına göre kullanıcı bul
    Optional<Kullanici> findByKullaniciAdi(String kullaniciAdi);
    
    // Email'e göre kullanıcı bul
    Optional<Kullanici> findByEmail(String email);
    
    // Kullanıcı adı veya email'e göre kullanıcı bul
    Optional<Kullanici> findByKullaniciAdiOrEmail(String kullaniciAdi, String email);
    
    // En yüksek puanlı kullanıcıları getir
    @Query("SELECT k FROM Kullanici k ORDER BY k.puan DESC")
    List<Kullanici> findTopKullanicilar();
    
    // Belirli bir seviyenin üstündeki kullanıcıları getir
    @Query("SELECT k FROM Kullanici k WHERE k.seviye >= :seviye")
    List<Kullanici> findBySeviyeUstundekiKullanicilar(@Param("seviye") Integer seviye);
    
    // Kullanıcı adı veya email ile arama yap
    @Query("SELECT k FROM Kullanici k WHERE k.kullaniciAdi LIKE %:arama% OR k.email LIKE %:arama%")
    List<Kullanici> searchKullanicilar(@Param("arama") String arama);
} 