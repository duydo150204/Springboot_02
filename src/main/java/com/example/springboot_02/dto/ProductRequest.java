package com.example.springboot_02.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "name is required")
        @Size(max = 150, message = "name must be at most 150 characters")
        String name,

        @Size(max = 2000, message = "description must be at most 2000 characters")
        String description,

        @NotNull(message = "price is required")
        @Positive(message = "price must be greater than 0")
        @Digits(integer = 8, fraction = 2, message = "price allows at most 8 digits and 2 decimals")
        BigDecimal price,

        // Integer (not int) so a MISSING value is null and triggers @NotNull,
        // instead of silently becoming 0.
        @NotNull(message = "stock is required")
        @PositiveOrZero(message = "stock cannot be negative")
        Integer stock,

        @NotNull(message = "categoryId is required")
        Long categoryId
) {
}
