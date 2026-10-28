package com.example.springboot_02.dto;

import com.example.springboot_02.entity.Category;

public record CategoryResponse(Long id, String name, String description) {

    // Entity -> DTO. Records can have static helper methods.
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getDescription());
    }
}
