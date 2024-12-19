package com.example.feedh.Repository;

import com.example.feedh.Model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// Ebtehal - Supplier Repository
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    Supplier findSupplierById(Integer id);
    List<Supplier> findSupplierByAddress(String address);

    //getSupplierByPrice
    @Query("SELECT s FROM Supplier s JOIN s.products p WHERE p.price <= :price")
    List<Supplier> findSuppliersByProductPrice(@Param("price") Double price);

    @Query("SELECT s FROM Supplier s JOIN s.heavyEquipments h WHERE h.pricePerHour <= :price")
    List<Supplier> findSuppliersByHeavyEquipmentRentPrice(@Param("price") Double price);
}

