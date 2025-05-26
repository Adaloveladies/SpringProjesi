package com.adaloveladies.SpringProjesi.repository;

import com.adaloveladies.SpringProjesi.model.Task;
import com.adaloveladies.SpringProjesi.model.TaskStatus;
import com.adaloveladies.SpringProjesi.model.User;
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
    List<Task> findByUser(User user);
    
    /**
     * Kullanıcıya ait ve belirli durumdaki görevleri getirir
     */
    List<Task> findByUserAndStatus(User user, TaskStatus status);
    
    /**
     * Kullanıcıya ait görevleri oluşturulma tarihine göre sıralar
     */
    List<Task> findByUserOrderByCreatedAtDesc(User user);
    
    /**
     * Kullanıcıya ait görevleri duruma ve tarihe göre sıralar
     */
    List<Task> findByUserAndStatusOrderByCreatedAtDesc(User user, TaskStatus status);
    
    /**
     * Kullanıcıya ait görevleri başlık veya açıklamada arama yapar
     */
    List<Task> findByUserAndTitleContainingOrDescriptionContaining(User user, String title, String description);
    
    // Günlük tamamlanan görev sayısını kontrol eden metod
    long countByUserAndStatusAndCompletedAtBetween(
        User user, 
        TaskStatus status, 
        LocalDateTime startDate, 
        LocalDateTime endDate
    );

    // Günlük oluşturulan görev sayısını kontrol eden metod
    long countByUserAndCreatedAtBetween(
        User user,
        LocalDateTime startDate,
        LocalDateTime endDate
    );

    int countByUserAndStatus(User user, TaskStatus status);

    List<Task> findByUserAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(User user, String searchTerm);
} 