package com.PetCaretopia.order.repository;


import com.PetCaretopia.order.entity.Product;
import com.PetCaretopia.order.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(ProductCategory category);

    List<Product> findByNameContainingIgnoreCase(String name);

    @Query("SELECT p FROM Product p WHERE p.stockQuantity > 0")
    List<Product> findAvailableProducts();
}
