package com.example.springboot_02.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;


public record CheckoutRequest(
        @NotNull(message = "customerId is required")
        Long customerId,

        // @Valid = also validate every element inside the list.
        @NotEmpty(message = "the cart must contain at least one item")
        @Valid
        List<Item> items
) {

    // A record nested inside a record: one cart line.
    public record Item(
            @NotNull(message = "productId is required")
            Long productId,

            @Positive(message = "quantity must be at least 1")
            int quantity
    ) {
    }
}
