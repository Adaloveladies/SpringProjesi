package com.adaloveladies.SpringProjesi.repository;

import com.adaloveladies.SpringProjesi.model.Gorev;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GorevRepository extends JpaRepository<Gorev, Long> {
    List<Gorev> findByKullaniciId(Long kullaniciId);
    List<Gorev> findByKullaniciIdAndTamamlandi(Long kullaniciId, boolean tamamlandi);
    List<Gorev> findByKullaniciAndTamamlandiAndTamamlanmaTarihiBetween(
        Kullanici kullanici, 
        boolean tamamlandi, 
        LocalDateTime baslangic, 
        LocalDateTime bitis
    );
    List<Gorev> findByTamamlandiAndTamamlanmaTarihiAfter(boolean tamamlandi, LocalDateTime tarih);
} 