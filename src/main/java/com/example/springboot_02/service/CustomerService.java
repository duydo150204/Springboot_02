package com.example.springboot_02.service;

import com.example.springboot_02.dto.CustomerRequest;
import com.example.springboot_02.dto.CustomerResponse;
import com.example.springboot_02.entity.Customer;
import com.example.springboot_02.exception.ConflictException;
import com.example.springboot_02.exception.ResourceNotFoundException;
import com.example.springboot_02.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customers;

    public CustomerService(CustomerRepository customers) {
        this.customers = customers;
    }

    public List<CustomerResponse> findAll() {
        return customers.findAll().stream().map(CustomerResponse::from).toList();
    }

    public CustomerResponse get(Long id) {
        return CustomerResponse.from(find(id));
    }

    @Transactional
    public CustomerResponse create(CustomerRequest request) {
        String email = normalize(request.email());
        if (customers.existsByEmail(email)) {
            throw new ConflictException("A customer with email '" + email + "' already exists");
        }
        Customer customer = new Customer(request.fullName().trim(), email, request.phone(), request.address());
        return CustomerResponse.from(customers.save(customer));
    }

    @Transactional
    public CustomerResponse update(Long id, CustomerRequest request) {
        Customer customer = find(id);
        String email = normalize(request.email());
        if (!customer.getEmail().equals(email) && customers.existsByEmail(email)) {
            throw new ConflictException("A customer with email '" + email + "' already exists");
        }
        customer.setFullName(request.fullName().trim());
        customer.setEmail(email);
        customer.setPhone(request.phone());
        customer.setAddress(request.address());
        return CustomerResponse.from(customer);
    }

    // Emails are stored lower-case so "Bob@x.com" and "bob@x.com" count as the same customer.
    private String normalize(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private Customer find(Long id) {
        return customers.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer", id));
    }
}
