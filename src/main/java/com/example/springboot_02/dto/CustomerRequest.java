package com.example.springboot_02.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerRequest(
        @NotBlank(message = "fullName is required")
        @Size(max = 150, message = "fullName must be at most 150 characters")
        String fullName,

        @NotBlank(message = "email is required")
        @Email(message = "email is not a valid address")
        @Size(max = 150, message = "email must be at most 150 characters")
        String email,

        @Size(max = 30, message = "phone must be at most 30 characters")
        String phone,

        @Size(max = 300, message = "address must be at most 300 characters")
        String address
) {
}
