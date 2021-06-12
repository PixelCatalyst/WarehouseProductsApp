package com.pixcat.warehouseproducts;

import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
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
                        .storageTemperature(Temperature.AMBIENT)
                        .height(Millimeters.of(20))
                        .width(Millimeters.of(30))
                        .length(Millimeters.of(40))
                        .weight(Kilograms.of(BigDecimal.valueOf(0.456)))
                        .build()
        );
        inMemoryProducts.put(
                ProductId.of("TEST-102"),
                ProductDetails.builder()
                        .id(ProductId.of("TEST-102"))
                        .description("Box of cornflakes")
                        .storageTemperature(Temperature.CHILL)
                        .height(Millimeters.of(120))
                        .width(Millimeters.of(230))
                        .length(Millimeters.of(340))
                        .weight(Kilograms.of(BigDecimal.valueOf(3.456)))
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
