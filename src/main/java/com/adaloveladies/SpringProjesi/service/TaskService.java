package com.adaloveladies.SpringProjesi.service;

import com.adaloveladies.SpringProjesi.dto.TaskRequestDTO;
import com.adaloveladies.SpringProjesi.dto.TaskResponseDTO;
import com.adaloveladies.SpringProjesi.exception.BusinessException;
import com.adaloveladies.SpringProjesi.exception.ResourceNotFoundException;
import com.adaloveladies.SpringProjesi.model.Building;
import com.adaloveladies.SpringProjesi.model.Task;
import com.adaloveladies.SpringProjesi.model.TaskStatus;
import com.adaloveladies.SpringProjesi.model.Kullanici;
import com.adaloveladies.SpringProjesi.repository.BuildingRepository;
import com.adaloveladies.SpringProjesi.repository.TaskRepository;
import com.adaloveladies.SpringProjesi.repository.KullaniciRepository;
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
    private final KullaniciRepository kullaniciRepository;
    private final BuildingRepository buildingRepository;

    // Puan hesaplama sabitleri
    private static final int BASE_POINTS = 10; // Temel puan
    private static final int LEVEL_UP_THRESHOLD = 50; // Seviye atlama eşiği (50 puan = 1 seviye)
    private static final int DAILY_TASK_LIMIT = 20; // Günlük görev limiti
    /**
     * TaskRequestDTO'yu Task modeline dönüştürür
     */
    private Task convertToTask(TaskRequestDTO dto) {
        return Task.builder()
                .baslik(dto.getBaslik())
                .aciklama(dto.getAciklama())
                .gorevTipi(dto.getGorevTipi())
                .puanDegeri(dto.getPuanDegeri())
                .sonTarih(dto.getBitisTarihi())
                .tamamlandi(false)
                .durum(TaskStatus.BEKLEMEDE)
                .build();
    }

    /**
     * Task modelini TaskResponseDTO'ya dönüştürür
     */
    private TaskResponseDTO convertToResponseDTO(Task task) {
        return TaskResponseDTO.builder()
                .id(task.getId())
                .baslik(task.getBaslik())
                .aciklama(task.getAciklama())
                .gorevTipi(task.getGorevTipi())
                .puanDegeri(task.getPuanDegeri())
                .bitisTarihi(task.getSonTarih())
                .tamamlandi(task.isTamamlandi())
                .olusturmaTarihi(task.getOlusturmaTarihi())
                .kullaniciAdi(task.getKullanici().getUsername())
                .build();
    }

    /**
     * Kullanıcının günlük görev oluşturma limitini kontrol eder
     */
    private void checkDailyTaskCreationLimit(Kullanici kullanici) {
        LocalDate today = LocalDate.now();
        long createdTasksToday = taskRepository.countByKullaniciAndOlusturmaTarihiBetween(
            kullanici,
            today.atStartOfDay(),
            today.plusDays(1).atStartOfDay()
        );

        if (createdTasksToday >= DAILY_TASK_LIMIT) {
            throw new BusinessException("Günlük görev oluşturma limitine ulaştınız. Yarın tekrar deneyin.");
        }
    }

    /**
     * Kullanıcının günlük görev limitini kontrol eder
     */
    private void checkDailyTaskLimit(Kullanici kullanici) {
        LocalDate today = LocalDate.now();
        long completedTasksToday = taskRepository.countByKullaniciAndDurumAndTamamlanmaTarihiBetween(
            kullanici, 
            TaskStatus.TAMAMLANDI,
            today.atStartOfDay(),
            today.plusDays(1).atStartOfDay()
        );

        if (completedTasksToday >= DAILY_TASK_LIMIT) {
            throw new BusinessException("Günlük görev limitine ulaştınız. Yarın tekrar deneyin.");
        }
    }

    /**
     * Yeni görev oluşturur
     */
    @Transactional
    public TaskResponseDTO createTask(TaskRequestDTO taskDTO, Kullanici kullanici) {
        checkDailyTaskCreationLimit(kullanici);

        Task task = convertToTask(taskDTO);
        task.setKullanici(kullanici);
        task.setDurum(TaskStatus.BEKLEMEDE);
        task.setOlusturmaTarihi(LocalDateTime.now());
        
        Task savedTask = taskRepository.save(task);
        return convertToResponseDTO(savedTask);
    }

    /**
     * Görevi günceller
     */
    @Transactional
    public TaskResponseDTO updateTask(Long taskId, TaskRequestDTO taskDTO, Kullanici kullanici) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Görev", "id", taskId));

        if (!existingTask.getKullanici().getId().equals(kullanici.getId())) {
            throw new BusinessException("Bu görevi güncelleme yetkiniz yok");
        }

        existingTask.setBaslik(taskDTO.getBaslik());
        existingTask.setAciklama(taskDTO.getAciklama());
        existingTask.setGorevTipi(taskDTO.getGorevTipi());

        Task updatedTask = taskRepository.save(existingTask);
        return convertToResponseDTO(updatedTask);
    }

    /**
     * Görevi siler
     */
    @Transactional
    public void deleteTask(Long taskId, Kullanici kullanici) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Görev", "id", taskId));

        if (!task.getKullanici().getId().equals(kullanici.getId())) {
            throw new BusinessException("Bu görevi silme yetkiniz yok");
        }

        taskRepository.delete(task);
    }

    /**
     * Kullanıcının tüm görevlerini getirir
     */
    public List<TaskResponseDTO> getAllTasks(Kullanici kullanici) {
        return taskRepository.findByKullanici(kullanici)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Kullanıcının belirli durumdaki görevlerini getirir
     */
    public List<TaskResponseDTO> getTasksByStatus(Kullanici kullanici, TaskStatus durum) {
        return taskRepository.findByKullaniciAndDurumOrderByOlusturmaTarihiDesc(kullanici, durum)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Görev tamamlandığında puan hesaplar ve kullanıcı seviyesini günceller
     */
    @Transactional
    private void calculatePointsAndUpdateLevel(Kullanici kullanici) {
        // Görev tamamlandığında temel puanı ekle
        kullanici.setPoints(kullanici.getPoints() + BASE_POINTS);

        // Seviye atlama kontrolü
        int newLevel = (kullanici.getPoints() / LEVEL_UP_THRESHOLD) + 1;
        if (newLevel > kullanici.getLevel()) {
            kullanici.setLevel(newLevel);
            
            // Seviye atlandığında bina inşaatını kontrol et
            Building nextBuilding = buildingRepository.findByKullaniciAndTamamlandiOrderByGerekliSeviyeAsc(kullanici, false)
                    .stream()
                    .findFirst()
                    .orElse(null);
            if (nextBuilding != null && nextBuilding.getGerekliSeviye() <= newLevel) {
                // Bina inşaatını tamamla
                nextBuilding.setTamamlandi(true);
                nextBuilding.setTamamlanmaTarihi(LocalDateTime.now());
                buildingRepository.save(nextBuilding);
            }
        }

        kullaniciRepository.save(kullanici);
    }

    /**
     * Görev durumunu günceller
     */
    @Transactional
    public TaskResponseDTO updateTaskStatus(Long taskId, TaskStatus newStatus, Kullanici kullanici) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Görev", "id", taskId));

        if (!task.getKullanici().getId().equals(kullanici.getId())) {
            throw new BusinessException("Bu görevi güncelleme yetkiniz yok");
        }

        // Eğer görev tamamlanıyorsa, günlük limit kontrolü yap
        if (newStatus == TaskStatus.TAMAMLANDI) {
            checkDailyTaskLimit(kullanici);
        }

        task.setDurum(newStatus);
        
        // Eğer görev tamamlandıysa, puan hesapla ve seviyeyi güncelle
        if (newStatus == TaskStatus.TAMAMLANDI) {
            task.setTamamlanmaTarihi(LocalDateTime.now());
            calculatePointsAndUpdateLevel(kullanici);
        }

        Task updatedTask = taskRepository.save(task);
        return convertToResponseDTO(updatedTask);
    }

    /**
     * Görevlerde arama yapar
     */
    public List<TaskResponseDTO> searchTasks(Kullanici kullanici, String searchTerm) {
        return taskRepository.findByKullaniciAndBaslikContainingIgnoreCaseOrderByOlusturmaTarihiDesc(kullanici, searchTerm)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public TaskResponseDTO getTaskById(Long taskId, Kullanici kullanici) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Görev", "id", taskId));
        if (!task.getKullanici().getId().equals(kullanici.getId())) {
            throw new BusinessException("Bu görevi görüntüleme yetkiniz yok");
        }
        return convertToResponseDTO(task);
    }
} 