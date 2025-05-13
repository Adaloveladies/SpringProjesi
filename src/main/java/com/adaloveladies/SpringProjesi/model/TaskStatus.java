package com.adaloveladies.SpringProjesi.model;

/**
 * Görev durumlarını temsil eden enum
 */
public enum TaskStatus {
    BEKLEMEDE,    // Görev henüz başlanmamış
    DEVAM_EDIYOR, // Görev üzerinde çalışılıyor
    TAMAMLANDI    // Görev tamamlandı
} 