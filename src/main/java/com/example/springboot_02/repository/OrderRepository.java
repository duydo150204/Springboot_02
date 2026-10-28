package com.example.springboot_02.repository;

import com.example.springboot_02.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // WHERE customer_id = ? ORDER BY created_at DESC
    List<Order> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
}
