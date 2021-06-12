package com.pixcat.warehouseproducts;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductRepository {

    private final Map<ProductId, ProductDetails> inMemoryProducts = new HashMap<>();

    private final JdbcTemplate jdbcTemplate;
    // TODO Persistence

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        inMemoryProducts.put(
                ProductId.of("TEST-101"),
                ProductDetails.builder()
                        .id(ProductId.of("TEST-101"))
                        .description("Can of cola")
                        .build()
        );
        inMemoryProducts.put(
                ProductId.of("TEST-102"),
                ProductDetails.builder()
                        .id(ProductId.of("TEST-102"))
                        .description("Box of cornflakes")
                        .build());
    }

    public List<ProductDetails> getAllProducts() {
        return new ArrayList<>(inMemoryProducts.values());
    }

    public ProductDetails getProduct(ProductId id) {
        return inMemoryProducts.get(id);
    }

    public boolean containsProduct(ProductId id) {
        return inMemoryProducts.containsKey(id);
    }

    public void createProduct(ProductId id, ProductDetails details) {
        inMemoryProducts.put(id, details);
    }
}
