package com.adaloveladies.SpringProjesi.repository;

import com.adaloveladies.SpringProjesi.model.Building;
import com.adaloveladies.SpringProjesi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
    List<Building> findByUserOrderByRequiredLevelAsc(User user);
    List<Building> findByUserAndIsCompletedFalseOrderByRequiredLevelAsc(User user);
    Building findFirstByUserAndIsCompletedFalseOrderByRequiredLevelAsc(User user);
} 