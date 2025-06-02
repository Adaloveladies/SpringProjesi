package com.adaloveladies.SpringProjesi.repository;

import com.adaloveladies.SpringProjesi.model.Task;
import com.adaloveladies.SpringProjesi.model.TaskStatus;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GorevRepository extends JpaRepository<Task, Long> {
    List<Task> findByKullanici(Kullanici kullanici);
    
    List<Task> findByKullaniciAndDurumOrderByOlusturmaTarihiDesc(Kullanici kullanici, TaskStatus durum);
    
    List<Task> findByKullaniciAndBaslikContainingIgnoreCaseOrderByOlusturmaTarihiDesc(Kullanici kullanici, String baslik);
    
    long countByKullaniciAndDurumAndTamamlanmaTarihiBetween(Kullanici kullanici, TaskStatus durum, LocalDateTime baslangic, LocalDateTime bitis);
    
    long countByKullanici(Kullanici kullanici);
    
    List<Task> findByKullaniciAndDurum(Kullanici kullanici, TaskStatus durum);
    
    long countByKullaniciAndDurum(Kullanici kullanici, TaskStatus durum);
    
    long countByDurum(TaskStatus durum);
    
    long countByTamamlanmaTarihiAfter(LocalDateTime tarih);
} 