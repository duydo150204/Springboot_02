package com.example.springboot_02.service;

import com.example.springboot_02.dto.ProductRequest;
import com.example.springboot_02.dto.ProductResponse;
import com.example.springboot_02.entity.Category;
import com.example.springboot_02.entity.Product;
import com.example.springboot_02.exception.ResourceNotFoundException;
import com.example.springboot_02.repository.CategoryRepository;
import com.example.springboot_02.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository products;
    private final CategoryRepository categories;

    public ProductService(ProductRepository products, CategoryRepository categories) {
        this.products = products;
        this.categories = categories;
    }

    // Both filters are optional: GET /api/products?keyword=mouse&categoryId=2
    public List<ProductResponse> search(String keyword, Long categoryId) {
        boolean hasKeyword = keyword != null && !keyword.isBlank();
        List<Product> found;
        if (hasKeyword && categoryId != null) {
            found = products.findByCategoryIdAndNameContainingIgnoreCase(categoryId, keyword.trim());
        } else if (hasKeyword) {
            found = products.findByNameContainingIgnoreCase(keyword.trim());
        } else if (categoryId != null) {
            found = products.findByCategoryId(categoryId);
        } else {
            found = products.findAll();
        }
        return found.stream().map(ProductResponse::from).toList();
    }

    public ProductResponse get(Long id) {
        return ProductResponse.from(find(id));
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        Category category = findCategory(request.categoryId());
        Product product = new Product(
                request.name().trim(),
                request.description(),
                request.price(),
                request.stock(),
                category);
        return ProductResponse.from(products.save(product));
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = find(id);
        product.setName(request.name().trim());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStock(request.stock());
        product.setCategory(findCategory(request.categoryId()));
        return ProductResponse.from(product);
    }

    @Transactional
    public void delete(Long id) {
        Product product = find(id);
        products.delete(product);
        // Send the DELETE now, so if an order still references this product the database error
        // happens here (and becomes a clean HTTP 409) rather than later at commit time.
        products.flush();
    }

    private Product find(Long id) {
        return products.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    private Category findCategory(Long categoryId) {
        return categories.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", categoryId));
    }
}
