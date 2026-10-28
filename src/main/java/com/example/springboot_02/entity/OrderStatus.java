package com.example.springboot_02.entity;

// The life of an order. Stored in the database as text ("PENDING"), see @Enumerated in Order.
public enum OrderStatus {
    PENDING,     // created at checkout, not paid yet
    PAID,
    SHIPPED,
    CANCELLED
}
