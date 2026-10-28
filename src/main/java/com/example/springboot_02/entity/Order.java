package com.example.springboot_02.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    // EnumType.STRING stores "PENDING" instead of 0, so the table stays readable in DBeaver
    // and reordering the enum later can't corrupt old rows.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;   // column name becomes created_at

    // One order has many lines. In C#: public List<OrderItem> Items { get; } = new();
    //  - mappedBy = the foreign key lives on OrderItem.order (this side is not the owner)
    //  - cascade ALL = saving/deleting the order also saves/deletes its lines
    //  - orphanRemoval = a line removed from this list is deleted from the database
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    // Runs just before the first INSERT. Like setting CreatedAt in SaveChanges().
    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Always add lines through this method so both sides of the relationship
    // (item.order and order.items) and the total stay in sync.
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
        total = total.add(item.getLineTotal());
    }

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<OrderItem> getItems() {
        return items;
    }
}
