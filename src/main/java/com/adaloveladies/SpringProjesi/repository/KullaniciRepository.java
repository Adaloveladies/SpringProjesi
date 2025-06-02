package com.adaloveladies.SpringProjesi.repository;

import com.adaloveladies.SpringProjesi.model.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KullaniciRepository extends JpaRepository<Kullanici, Long> {
    
    // Kullanıcı adına göre kullanıcı bul
    Optional<Kullanici> findByUsername(String username);
    
    // Email'e göre kullanıcı bul
    Optional<Kullanici> findByEmail(String email);
    
    // Kullanıcı adı veya email'e göre kullanıcı bul
    Optional<Kullanici> findByUsernameOrEmail(String username, String email);
    
    // En yüksek puanlı kullanıcıları getir
    @Query("SELECT k FROM Kullanici k ORDER BY k.points DESC LIMIT 10")
    List<Kullanici> findTopKullanicilar();
    
    // Belirli bir seviyenin üstündeki kullanıcıları getir
    @Query("SELECT k FROM Kullanici k WHERE k.level >= :seviye")
    List<Kullanici> findBySeviyeUstundekiKullanicilar(@Param("seviye") Integer seviye);
    
    // Kullanıcı adı veya email ile arama yap
    @Query("SELECT k FROM Kullanici k WHERE k.username LIKE %:arama% OR k.email LIKE %:arama%")
    List<Kullanici> searchKullanicilar(@Param("arama") String arama);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    List<Kullanici> findTop10ByOrderByPointsDesc();
    List<Kullanici> findTop10ByOrderByCompletedTaskCountDesc();
    long countByPointsGreaterThan(Integer points);
} 