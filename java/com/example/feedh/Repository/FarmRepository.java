package com.example.feedh.Repository;

import com.example.feedh.Model.Customer;
import com.example.feedh.Model.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Nawaf - Farm Repository
@Repository
public interface FarmRepository extends JpaRepository<Farm, Integer> {
    Farm findFarmById(Integer id);

    List<Farm> findFarmByLocation(String location);

    List<Farm> findFarmByCustomer(Customer customer);

    List<Farm> findFarmByCustomerAndType(Customer customer, String type);
}
