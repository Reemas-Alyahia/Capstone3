package com.example.feedh.Repository;

import com.example.feedh.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// Reemas - Product Repository
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findProductById(Integer id);

    List<Product> findProductByCategory(String category);

    @Query("select p from Product p where p.price >= ?1")
    List<Product> getProductByPriceMoreThanOrEqual(Double price);

    @Query("select p from Product p where p.price <= ?1")
    List<Product> getProductByPriceLessThanOrEqual(Double price);

    @Query("SELECT p FROM Product p WHERE p.supplier.id = :supplierId AND p.quantity < :quantityThreshold")
    List<Product> findLowQuantityProductsBySupplierId(@Param("supplierId") Integer supplierId, @Param("quantityThreshold") Integer quantityThreshold);
}
