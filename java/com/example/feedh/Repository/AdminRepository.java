package com.example.feedh.Repository;

import com.example.feedh.Model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Nawaf - Admin Repository
@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findAdminById(Integer id);
}
