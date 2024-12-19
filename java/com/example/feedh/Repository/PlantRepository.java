package com.example.feedh.Repository;

import com.example.feedh.Model.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Ebtehal - Plant Repository
@Repository
public interface PlantRepository extends JpaRepository<Plant, Integer> {
    Plant findPlantById(Integer Id);
    Optional<Plant> findByFarm_IdAndType(Integer farmId, String type);
}
