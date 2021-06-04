package com.pixcat.warehouseproducts;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/v1")
public class ProductController {

    private final Map<ProductId, ProductDetails> inMemoryProducts = new HashMap<>();

    public ProductController() {
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

    @Secured("ROLE_GET_ALL_PRODUCTS")
    @GetMapping(value = "/products", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        final var productDtos = inMemoryProducts.values().stream()
                .map(ProductDto::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDtos);
    }

    @GetMapping(value = "/products/{productId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDto> getProduct(@PathVariable("productId") ProductId productId) {
        final var product = inMemoryProducts.get(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ProductDto.of(product));
    }

    @PutMapping(value = "/products/{productId}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> putProduct(
            @PathVariable("productId") ProductId productId,
            @RequestBody ProductDto productDto) {
        final var product = ProductDetails.of(productDto);
        if (!product.getId().equals(productId)) {
            return ResponseEntity.badRequest().build();
        }
        inMemoryProducts.put(productId, product);

        return ResponseEntity.ok().build();
    }
}
