package com.example.springboot_02.dto;

import com.example.springboot_02.entity.Product;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        int stock,
        Long categoryId,
        String categoryName
) {

    // Call this INSIDE a @Transactional service method: getCategory() is lazy and needs
    // the database session to still be open.
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory().getId(),
                product.getCategory().getName());
    }
}
