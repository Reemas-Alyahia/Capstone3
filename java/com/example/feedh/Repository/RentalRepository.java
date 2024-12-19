package com.example.feedh.Repository;

import com.example.feedh.Model.Customer;
import com.example.feedh.Model.Rental;
import com.example.feedh.Model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

// Reemas - Rental Repository
@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {
    Rental findRentalById(Integer id);


    @Query("SELECT r FROM Rental r WHERE r.endDateTime <= :now AND r.status = :status")
    List<Rental> getRentalByRentalEndDateAndStatus(@Param("now") LocalDateTime now, @Param("status") String status);

    List<Rental> findByEndDateTimeBetween(LocalDateTime start, LocalDateTime end);


    @Query("select r from Rental r where r.price=?1 and r.status=?2")
    List<Rental> getRentalByPriceAndStatus(Double price, String status);


//    @Query("SELECT COUNT(r) FROM Rental r WHERE r.customer.id = :customerId AND r.heavyEquipments.supplier.id = :supplierId")
//    Integer countRentalsByCustomerAndSupplier(@Param("customerId") Integer customerId, @Param("supplierId") Integer supplierId);
}