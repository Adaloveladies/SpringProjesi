package com.adaloveladies.SpringProjesi.model;

/**
 * Görev durumlarını temsil eden enum
 */
public enum TaskStatus {
    BEKLEMEDE,    // Görev henüz başlamadı
    DEVAM_EDIYOR,  // Görev devam ediyor
    TAMAMLANDI,    // Görev tamamlandı
    IPTAL_EDILDI    // Görev iptal edildi
} 