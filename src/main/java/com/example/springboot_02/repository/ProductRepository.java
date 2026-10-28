package com.example.springboot_02.repository;

import com.example.springboot_02.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // WHERE LOWER(name) LIKE LOWER('%keyword%')   (like .Where(p => p.Name.Contains(keyword)))
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // "CategoryId" walks the relationship: product.category.id
    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByCategoryIdAndNameContainingIgnoreCase(Long categoryId, String keyword);

    // Used to block deleting a category that still has products.
    boolean existsByCategoryId(Long categoryId);
}
