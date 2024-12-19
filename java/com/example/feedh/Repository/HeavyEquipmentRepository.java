package com.example.feedh.Repository;

import com.example.feedh.Model.HeavyEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Ebtehal - HeavyEquipment Repository
@Repository
public interface HeavyEquipmentRepository extends JpaRepository<HeavyEquipment, Integer> {
    HeavyEquipment findHeavyEquipmentById(Integer id);

    List<HeavyEquipment> findHeavyEquipmentByStatus(String status);
}
