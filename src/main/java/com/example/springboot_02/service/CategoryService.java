package com.example.springboot_02.service;

import com.example.springboot_02.dto.CategoryRequest;
import com.example.springboot_02.dto.CategoryResponse;
import com.example.springboot_02.entity.Category;
import com.example.springboot_02.exception.ConflictException;
import com.example.springboot_02.exception.ResourceNotFoundException;
import com.example.springboot_02.repository.CategoryRepository;
import com.example.springboot_02.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// @Service = a business-logic class that Spring creates once and injects where needed, like a
// class registered with services.AddScoped<>() in ASP.NET.
// @Transactional at class level = every method runs in a database transaction (readOnly by
// default here); methods that change data override it with a plain @Transactional.
@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categories;
    private final ProductRepository products;

    // Constructor injection, just like ASP.NET Core DI.
    public CategoryService(CategoryRepository categories, ProductRepository products) {
        this.categories = categories;
        this.products = products;
    }

    public List<CategoryResponse> findAll() {
        return categories.findAll().stream().map(CategoryResponse::from).toList();
    }

    public CategoryResponse get(Long id) {
        return CategoryResponse.from(find(id));
    }

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        String name = request.name().trim();
        if (categories.existsByNameIgnoreCase(name)) {
            throw new ConflictException("A category named '" + name + "' already exists");
        }
        Category saved = categories.save(new Category(name, request.description()));
        return CategoryResponse.from(saved);
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = find(id);
        String name = request.name().trim();
        if (!category.getName().equalsIgnoreCase(name) && categories.existsByNameIgnoreCase(name)) {
            throw new ConflictException("A category named '" + name + "' already exists");
        }
        // No save() call needed: "category" is a managed entity, so Hibernate notices the
        // changes and writes the UPDATE when the transaction commits (like EF's change tracking).
        category.setName(name);
        category.setDescription(request.description());
        return CategoryResponse.from(category);
    }

    @Transactional
    public void delete(Long id) {
        Category category = find(id);
        if (products.existsByCategoryId(id)) {
            throw new ConflictException("Category '" + category.getName() + "' still has products; move or delete them first");
        }
        categories.delete(category);
    }

    private Category find(Long id) {
        return categories.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", id));
    }
}
