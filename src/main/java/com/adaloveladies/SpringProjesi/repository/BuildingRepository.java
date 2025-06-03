package com.adaloveladies.SpringProjesi.repository;

import com.adaloveladies.SpringProjesi.model.Building;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
    List<Building> findByKullanici(Kullanici kullanici);
    
    List<Building> findByKullaniciAndTamamlandiOrderByGerekliSeviyeAsc(Kullanici kullanici, Boolean tamamlandi);
    
    long countByKullaniciAndTamamlandi(Kullanici kullanici, Boolean tamamlandi);
    
    List<Building> findByGerekliSeviyeOrderByGerekliSeviyeAsc(Integer gerekliSeviye);

    List<Building> findByKullaniciId(Long kullaniciId);
    List<Building> findByKullaniciIdAndTamamlandi(Long kullaniciId, boolean tamamlandi);
} 