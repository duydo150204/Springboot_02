package com.example.springboot_02.repository;

import com.example.springboot_02.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

// interface
public interface CategoryRepository extends JpaRepository<Category, Long> {


    boolean existsByNameIgnoreCase(String name);
}
