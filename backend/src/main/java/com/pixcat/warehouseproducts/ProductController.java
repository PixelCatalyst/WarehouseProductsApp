package com.pixcat.warehouseproducts;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/v1")
public class ProductController {

    private final static String ALL_IMAGES_MEDIA_TYPE = "image/*";

    private final Map<ProductId, ProductDetails> inMemoryProducts = new HashMap<>();
    private final ImageRepository imageRepository;

    public ProductController(@Autowired ImageRepository imageRepository) {
        this.imageRepository = imageRepository;

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

    @Secured("ROLE_READ_BULK_PRODUCTS")
    @GetMapping(value = "/products", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        log.info("Get all products");

        final var productDtos = inMemoryProducts.values().stream()
                .map(ProductDto::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDtos);
    }

    @Secured("ROLE_READ_PRODUCTS")
    @GetMapping(value = "/products/{productId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDto> getProduct(@PathVariable("productId") ProductId productId) {
        log.info("Get product {}", productId);

        final var product = inMemoryProducts.get(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ProductDto.of(product));
    }

    @Secured("ROLE_WRITE_PRODUCTS")
    @PutMapping(value = "/products/{productId}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> putProduct(
            @PathVariable("productId") ProductId productId,
            @RequestBody ProductDto productDto) {
        log.info("Put product {}", productId);

        final var product = ProductDetails.of(productDto);
        if (!product.getId().equals(productId)) {
            return ResponseEntity.badRequest().build();
        }
        inMemoryProducts.put(productId, product);

        return ResponseEntity.ok().build();
    }

    @Secured("ROLE_WRITE_PRODUCTS")
    @PutMapping(value = "/products/{productId}/image", consumes = ALL_IMAGES_MEDIA_TYPE)
    public ResponseEntity<Void> putProductImage(
            @PathVariable("productId") ProductId productId,
            @RequestHeader(value = "Content-Type") String contentType,
            InputStream image) {
        log.info("Put product {} image", productId);

        if (inMemoryProducts.containsKey(productId)) {
            imageRepository.saveImage(productId, ImagePayload.of(image, contentType));
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
