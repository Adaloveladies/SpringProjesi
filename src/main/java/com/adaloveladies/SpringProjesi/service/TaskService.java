package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.dto.TaskRequestDTO;
import com.adaloveladies.SpringProjesi.dto.TaskResponseDTO;
import com.adaloveladies.SpringProjesi.model.Building;
import com.adaloveladies.SpringProjesi.model.Task;
import com.adaloveladies.SpringProjesi.model.TaskStatus;
import com.adaloveladies.SpringProjesi.model.User;
import com.adaloveladies.SpringProjesi.repository.BuildingRepository;
import com.adaloveladies.SpringProjesi.repository.TaskRepository;
import com.adaloveladies.SpringProjesi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Görev işlemlerini yöneten servis
 */
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final BuildingRepository buildingRepository;

    // Puan hesaplama sabitleri
    private static final int BASE_POINTS = 10; // Temel puan
    private static final int LEVEL_UP_THRESHOLD = 50; // Seviye atlama eşiği (50 puan = 1 seviye)
    private static final int DAILY_TASK_LIMIT = 20; // Günlük görev limiti
    private static final int MAX_FLOORS_PER_BUILDING = 10; // Bina başına maksimum kat sayısı

    /**
     * TaskRequestDTO'yu Task modeline dönüştürür
     */
    private Task convertToTask(TaskRequestDTO dto) {
        return Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .build();
    }

    /**
     * Task modelini TaskResponseDTO'ya dönüştürür
     */
    private TaskResponseDTO convertToResponseDTO(Task task) {
        return TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .createdAt(task.getCreatedAt())
                .completedAt(task.getCompletedAt())
                .userId(task.getUser().getId())
                .username(task.getUser().getUsername())
                .build();
    }

    /**
     * Kullanıcının günlük görev oluşturma limitini kontrol eder
     */
    private void checkDailyTaskCreationLimit(User user) {
        LocalDate today = LocalDate.now();
        long createdTasksToday = taskRepository.countByUserAndCreatedAtBetween(
            user,
            today.atStartOfDay(),
            today.plusDays(1).atStartOfDay()
        );

        if (createdTasksToday >= DAILY_TASK_LIMIT) {
            throw new RuntimeException("Günlük görev oluşturma limitine ulaştınız. Yarın tekrar deneyin.");
        }
    }

    /**
     * Kullanıcının günlük görev limitini kontrol eder
     */
    private void checkDailyTaskLimit(User user) {
        LocalDate today = LocalDate.now();
        long completedTasksToday = taskRepository.countByUserAndStatusAndCompletedAtBetween(
            user, 
            TaskStatus.TAMAMLANDI,
            today.atStartOfDay(),
            today.plusDays(1).atStartOfDay()
        );

        if (completedTasksToday >= DAILY_TASK_LIMIT) {
            throw new RuntimeException("Günlük görev limitine ulaştınız. Yarın tekrar deneyin.");
        }
    }

    /**
     * Yeni görev oluşturur
     */
    @Transactional
    public TaskResponseDTO createTask(TaskRequestDTO taskDTO, User user) {
        // Günlük görev oluşturma limitini kontrol et
        checkDailyTaskCreationLimit(user);

        Task task = convertToTask(taskDTO);
        task.setUser(user);
        task.setStatus(TaskStatus.BEKLEMEDE);
        Task savedTask = taskRepository.save(task);
        return convertToResponseDTO(savedTask);
    }

    /**
     * Görevi günceller
     */
    @Transactional
    public TaskResponseDTO updateTask(Long taskId, TaskRequestDTO taskDTO, User user) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Görev bulunamadı"));

        if (!existingTask.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bu görevi güncelleme yetkiniz yok");
        }

        existingTask.setTitle(taskDTO.getTitle());
        existingTask.setDescription(taskDTO.getDescription());
        existingTask.setStatus(taskDTO.getStatus());

        Task updatedTask = taskRepository.save(existingTask);
        return convertToResponseDTO(updatedTask);
    }

    /**
     * Görevi siler
     */
    @Transactional
    public void deleteTask(Long taskId, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Görev bulunamadı"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bu görevi silme yetkiniz yok");
        }

        taskRepository.delete(task);
    }

    /**
     * Kullanıcının tüm görevlerini getirir
     */
    public List<TaskResponseDTO> getAllTasksByUser(User user) {
        return taskRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Kullanıcının belirli durumdaki görevlerini getirir
     */
    public List<TaskResponseDTO> getTasksByStatus(User user, TaskStatus status) {
        return taskRepository.findByUserAndStatusOrderByCreatedAtDesc(user, status)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Görev tamamlandığında puan hesaplar ve kullanıcı seviyesini günceller
     */
    @Transactional
    private void calculatePointsAndUpdateLevel(User user) {
        // Görev tamamlandığında temel puanı ekle
        user.setScore(user.getScore() + BASE_POINTS);

        // Seviye atlama kontrolü
        int newLevel = (user.getScore() / LEVEL_UP_THRESHOLD) + 1;
        if (newLevel > user.getLevel()) {
            user.setLevel(newLevel);
            
            // Seviye atlandığında bina inşaatını kontrol et
            Building nextBuilding = buildingRepository.findFirstByUserAndIsCompletedFalseOrderByRequiredLevelAsc(user);
            if (nextBuilding != null && nextBuilding.getRequiredLevel() <= newLevel) {
                // Bina inşaatını tamamla
                nextBuilding.setIsCompleted(true);
                nextBuilding.setCompletedAt(LocalDateTime.now());
                buildingRepository.save(nextBuilding);
            }
        }

        userRepository.save(user);
    }

    /**
     * Görev durumunu günceller
     */
    @Transactional
    public TaskResponseDTO updateTaskStatus(Long taskId, TaskStatus newStatus, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Görev bulunamadı"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bu görevi güncelleme yetkiniz yok");
        }

        // Eğer görev tamamlanıyorsa, günlük limit kontrolü yap
        if (newStatus == TaskStatus.TAMAMLANDI) {
            checkDailyTaskLimit(user);
        }

        task.setStatus(newStatus);
        
        // Eğer görev tamamlandıysa, puan hesapla ve seviyeyi güncelle
        if (newStatus == TaskStatus.TAMAMLANDI) {
            task.setCompletedAt(LocalDateTime.now());
            calculatePointsAndUpdateLevel(user);
        }

        Task updatedTask = taskRepository.save(task);
        return convertToResponseDTO(updatedTask);
    }

    /**
     * Görevlerde arama yapar
     */
    public List<TaskResponseDTO> searchTasks(User user, String searchTerm) {
        return taskRepository.findByUserAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(user, searchTerm)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    private void updateBuildingProgress(User user) {
        // Günlük tamamlanan görev sayısını al
        LocalDate today = LocalDate.now();
        long completedTasksToday = taskRepository.countByUserAndStatusAndCompletedAtBetween(
            user, 
            TaskStatus.TAMAMLANDI,
            today.atStartOfDay(),
            today.plusDays(1).atStartOfDay()
        );

        // Aktif binayı bul (tamamlanmamış ve en son oluşturulan)
        Building activeBuilding = buildingRepository.findFirstByUserAndIsCompletedFalseOrderByIdDesc(user);
        
        if (activeBuilding == null) {
            // Yeni bina oluştur
            activeBuilding = Building.builder()
                    .name("Yeni Bina")
                    .description("Yeni inşa edilen bina")
                    .requiredLevel(1)
                    .floorCount(0)
                    .dailyCompletedTasks(0)
                    .isCompleted(false)
                    .hasRoof(false)
                    .user(user)
                    .build();
            activeBuilding = buildingRepository.save(activeBuilding);
        }

        // Günlük tamamlanan görev sayısını artır
        activeBuilding.setDailyCompletedTasks((int) completedTasksToday);

        // Kat sayısını artır
        activeBuilding.setFloorCount(activeBuilding.getFloorCount() + 1);

        // Eğer bina 10 kata ulaştıysa
        if (activeBuilding.getFloorCount() >= MAX_FLOORS_PER_BUILDING) {
            // Binayı tamamla ve çatı ekle
            activeBuilding.setIsCompleted(true);
            activeBuilding.setHasRoof(true);
            activeBuilding.setCompletedAt(LocalDateTime.now());
            
            // Eğer hala tamamlanmamış görevler varsa, yeni bir bina başlat
            if (completedTasksToday < DAILY_TASK_LIMIT) {
                Building newBuilding = Building.builder()
                        .name("Yeni Bina")
                        .description("Yeni inşa edilen bina")
                        .requiredLevel(activeBuilding.getRequiredLevel() + 1)
                        .floorCount(0)
                        .dailyCompletedTasks((int) completedTasksToday)
                        .isCompleted(false)
                        .hasRoof(false)
                        .user(user)
                        .build();
                buildingRepository.save(newBuilding);
            }
        }
        // Eğer günlük görevler tamamlandıysa ve bina 10 kata ulaşmadıysa
        else if (completedTasksToday >= DAILY_TASK_LIMIT) {
            // Binayı tamamla ve çatı ekle
            activeBuilding.setIsCompleted(true);
            activeBuilding.setHasRoof(true);
            activeBuilding.setCompletedAt(LocalDateTime.now());
        }

        buildingRepository.save(activeBuilding);
    }
} 