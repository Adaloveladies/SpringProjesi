package com.adaloveladies.SpringProjesi.repository;

import com.adaloveladies.SpringProjesi.model.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KullaniciRepository extends JpaRepository<Kullanici, Long> {
    
    Optional<Kullanici> findByUsername(String username);
    Optional<Kullanici> findByEmail(String email);
    Optional<Kullanici> findByUsernameOrEmail(String username, String email);
    
    // Buradaki @Query içindeki LIMIT ifadesi JPA desteklemediği için kaldırıldı.
    // Bu metot Spring Data JPA'nın method name query özelliğiyle otomatik çalışacak:
    List<Kullanici> findTop10ByOrderByPointsDesc();
    
    List<Kullanici> findTop10ByOrderByCompletedTaskCountDesc();
    
    long countByPointsGreaterThan(Integer points);
    
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    @Query("SELECT k FROM Kullanici k ORDER BY k.points DESC")
    List<Kullanici> findTopKullanicilar();
    
    // Diğer @Query metotlar bu haliyle kalabilir.
}
