package com.example.springboot_02.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 2000)
    private String description;

    // BigDecimal, never double/float, for money (like decimal in C#).
    // precision 10, scale 2 = up to 99,999,999.99
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private int stock;

    // Many products belong to one category. In C#: public Category Category { get; set; }
    // LAZY = the category row is only loaded when you actually call getCategory().getName(),
    // so it must happen inside a transaction (our service classes are @Transactional).
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    // Optimistic locking, like a [Timestamp]/RowVersion concurrency token in EF Core.
    // Hibernate adds "version" to every UPDATE ... WHERE version = ?. If two customers buy
    // the last item at the same moment, the second UPDATE matches 0 rows and fails with an
    // ObjectOptimisticLockingFailureException (we turn that into HTTP 409) instead of
    // silently selling more items than we have.
    @Version
    private Long version;

    protected Product() {
    }

    public Product(String name, String description, BigDecimal price, int stock, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
