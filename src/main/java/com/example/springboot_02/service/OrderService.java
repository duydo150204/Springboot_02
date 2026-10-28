package com.example.springboot_02.service;

import com.example.springboot_02.dto.CheckoutRequest;
import com.example.springboot_02.dto.OrderResponse;
import com.example.springboot_02.entity.Customer;
import com.example.springboot_02.entity.Order;
import com.example.springboot_02.entity.OrderItem;
import com.example.springboot_02.entity.OrderStatus;
import com.example.springboot_02.entity.Product;
import com.example.springboot_02.exception.ConflictException;
import com.example.springboot_02.exception.ResourceNotFoundException;
import com.example.springboot_02.repository.CustomerRepository;
import com.example.springboot_02.repository.OrderRepository;
import com.example.springboot_02.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orders;
    private final CustomerRepository customers;
    private final ProductRepository products;

    public OrderService(OrderRepository orders, CustomerRepository customers, ProductRepository products) {
        this.orders = orders;
        this.customers = customers;
        this.products = products;
    }

    // THE core business method of the shop. Everything below runs in ONE transaction:
    // if any line fails (unknown product, not enough stock) the exception rolls back every
    // change, so we never end up with a half-created order or stock taken for nothing.
    // Like wrapping the work in a TransactionScope / DbContext.Database.BeginTransaction().
    @Transactional
    public OrderResponse checkout(CheckoutRequest request) {
        Customer customer = customers.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", request.customerId()));

        Order order = new Order();
        order.setCustomer(customer);

        for (CheckoutRequest.Item line : request.items()) {
            Product product = products.findById(line.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", line.productId()));

            if (product.getStock() < line.quantity()) {
                throw new ConflictException("Not enough stock for '" + product.getName()
                        + "': requested " + line.quantity() + ", available " + product.getStock());
            }

            // Take the items out of stock. "product" is a managed entity, so this change is saved
            // automatically. If the same product appears twice in the cart, findById returns the
            // SAME object, so the second stock check already sees the reduced amount.
            product.setStock(product.getStock() - line.quantity());

            // Copy the current price into the line (see OrderItem.unitPrice).
            order.addItem(new OrderItem(product, line.quantity(), product.getPrice()));
        }

        // saveAndFlush = INSERT the order and UPDATE the stock rows right now, so a concurrent
        // purchase of the same product fails here (optimistic locking) and becomes HTTP 409.
        Order saved = orders.saveAndFlush(order);
        return OrderResponse.from(saved);
    }

    public OrderResponse get(Long id) {
        return OrderResponse.from(find(id));
    }

    public List<OrderResponse> findByCustomer(Long customerId) {
        if (!customers.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer", customerId);
        }
        return orders.findByCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .map(OrderResponse::from)
                .toList();
    }

    // Only an unpaid order can be cancelled; the reserved stock goes back on the shelf.
    @Transactional
    public OrderResponse cancel(Long id) {
        Order order = find(id);
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new ConflictException("Only PENDING orders can be cancelled; order " + id
                    + " is " + order.getStatus());
        }
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
        }
        order.setStatus(OrderStatus.CANCELLED);
        return OrderResponse.from(order);
    }

    private Order find(Long id) {
        return orders.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", id));
    }
}
