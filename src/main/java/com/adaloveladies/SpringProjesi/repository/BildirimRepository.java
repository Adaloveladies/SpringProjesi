package com.adaloveladies.SpringProjesi.repository;

import com.adaloveladies.SpringProjesi.model.Bildirim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BildirimRepository extends JpaRepository<Bildirim, Long> {
    List<Bildirim> findByKullaniciIdOrderByOlusturmaTarihiDesc(Long kullaniciId);
    List<Bildirim> findByKullaniciIdAndOkunduFalseOrderByOlusturmaTarihiDesc(Long kullaniciId);
    Long countByKullaniciIdAndOkunduFalse(Long kullaniciId);
} 