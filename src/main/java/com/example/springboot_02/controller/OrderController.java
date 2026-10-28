package com.example.springboot_02.controller;

import com.example.springboot_02.dto.CheckoutRequest;
import com.example.springboot_02.dto.OrderResponse;
import com.example.springboot_02.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // POST /api/orders   body: { "customerId": 1, "items": [ { "productId": 1, "quantity": 2 } ] }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse checkout(@Valid @RequestBody CheckoutRequest request) {
        return orderService.checkout(request);
    }

    // GET /api/orders/1
    @GetMapping("/{id}")
    public OrderResponse get(@PathVariable Long id) {
        return orderService.get(id);
    }

    // GET /api/orders?customerId=1   (that customer's orders, newest first)
    @GetMapping
    public List<OrderResponse> listForCustomer(@RequestParam Long customerId) {
        return orderService.findByCustomer(customerId);
    }

    // POST /api/orders/1/cancel
    @PostMapping("/{id}/cancel")
    public OrderResponse cancel(@PathVariable Long id) {
        return orderService.cancel(id);
    }
}
