package com.example.feedh.Repository;

import com.example.feedh.Model.Customer;
import com.example.feedh.Model.OrderFD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Reemas - OrderFD Repository
@Repository
public interface OrderFDRepository extends JpaRepository<OrderFD, Integer> {
    OrderFD findOrderFDById(Integer id);

    List<OrderFD> findOrderFDByCustomer(Customer customer);
}
