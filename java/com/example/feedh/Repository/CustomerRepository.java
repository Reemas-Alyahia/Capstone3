package com.example.feedh.Repository;

import com.example.feedh.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Reemas - Customer Repository
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findCustomerById(Integer id);
}
