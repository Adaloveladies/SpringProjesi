package com.adaloveladies.SpringProjesi.repository;

import com.adaloveladies.SpringProjesi.model.Task;
import com.adaloveladies.SpringProjesi.model.TaskStatus;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Görev veritabanı işlemlerini yöneten repository
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    /**
     * Kullanıcıya ait tüm görevleri getirir
     */
    List<Task> findByKullanici(Kullanici kullanici);
    
    /**
     * Kullanıcıya ait görevleri başlık veya açıklamada arama yapar
     */
    List<Task> findByKullaniciAndBaslikContainingIgnoreCaseOrderByOlusturmaTarihiDesc(Kullanici kullanici, String searchTerm);
    
    /**
     * Kullanıcıya ait görevleri oluşturulma tarihine göre sıralar
     */
    List<Task> findByKullaniciOrderByOlusturmaTarihiDesc(Kullanici kullanici);
    
    /**
     * Kullanıcıya ait görevleri duruma ve tarihe göre sıralar
     */
    List<Task> findByKullaniciAndDurumOrderByOlusturmaTarihiDesc(Kullanici kullanici, TaskStatus durum);
    
    /**
     * Kullanıcıya ait görevleri başlık veya açıklamada arama yapar
     */
    List<Task> findByKullaniciAndBaslikContainingOrAciklamaContaining(Kullanici kullanici, String baslik, String aciklama);
    
    // Günlük tamamlanan görev sayısını kontrol eden metod
    long countByKullaniciAndOlusturmaTarihiBetween(Kullanici kullanici, LocalDateTime start, LocalDateTime end);

    // Günlük oluşturulan görev sayısını kontrol eden metod
    long countByKullaniciAndDurumAndTamamlanmaTarihiBetween(Kullanici kullanici, TaskStatus durum, LocalDateTime start, LocalDateTime end);

    int countByKullaniciAndDurum(Kullanici kullanici, TaskStatus durum);

    List<Task> findByKullaniciAndBaslikContainingIgnoreCase(Kullanici kullanici, String baslik);
} 