package com.example.feedh.Repository;

import com.example.feedh.Model.Customer;
import com.example.feedh.Model.Farm;
import com.example.feedh.Model.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Nawaf - Farmer Repository
@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Integer> {
    Farmer findFarmerById(Integer id);

    List<Farmer> findFarmerByCustomer(Customer customer);

    List<Farmer> findFarmerByCustomerAndFarm(Customer customer, Farm farm);

    List<Farmer> findFarmerByCustomerAndVisaType(Customer customer, String visaType);
}
