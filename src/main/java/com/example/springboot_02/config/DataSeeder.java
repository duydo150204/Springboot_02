package com.example.springboot_02.config;

import com.example.springboot_02.entity.Category;
import com.example.springboot_02.entity.Customer;
import com.example.springboot_02.entity.Product;
import com.example.springboot_02.repository.CategoryRepository;
import com.example.springboot_02.repository.CustomerRepository;
import com.example.springboot_02.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;


@Component
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categories;
    private final ProductRepository products;
    private final CustomerRepository customers;

    public DataSeeder(CategoryRepository categories, ProductRepository products, CustomerRepository customers) {
        this.categories = categories;
        this.products = products;
        this.customers = customers;
    }

    @Override
    public void run(String... args) {
        if (categories.count() > 0) {
            return;
        }

        Category electronics = categories.save(new Category("Electronics", "Phones, laptops and accessories"));
        Category books = categories.save(new Category("Books", "Programming and general reading"));
        Category home = categories.save(new Category("Home & Kitchen", "Everyday items for the home"));

        products.saveAll(List.of(
                new Product("Wireless Mouse", "Ergonomic 2.4 GHz wireless mouse", new BigDecimal("19.90"), 50, electronics),
                new Product("Mechanical Keyboard", "Hot-swappable, blue switches", new BigDecimal("79.00"), 30, electronics),
                new Product("USB-C Hub", "7-in-1 hub with HDMI and card reader", new BigDecimal("34.50"), 40, electronics),
                new Product("Noise-Cancelling Headphones", "Over-ear Bluetooth headphones", new BigDecimal("129.99"), 15, electronics),
                new Product("Effective Java", "Joshua Bloch, 3rd edition", new BigDecimal("42.00"), 25, books),
                new Product("Spring in Action", "Craig Walls, the Spring Boot classic", new BigDecimal("39.95"), 20, books),
                new Product("Ceramic Coffee Mug", "350 ml, dishwasher safe", new BigDecimal("8.50"), 100, home),
                new Product("Stainless Steel Water Bottle", "750 ml, keeps drinks cold for 24h", new BigDecimal("15.00"), 5, home)));

        customers.save(new Customer("Demo Customer", "demo@example.com", "0900000000", "1 Demo Street, Ho Chi Minh City"));
    }
}
