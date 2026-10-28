package com.example.springboot_02.dto;

import com.example.springboot_02.entity.Customer;

public record CustomerResponse(Long id, String fullName, String email, String phone, String address) {

    public static CustomerResponse from(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFullName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress());
    }
}
