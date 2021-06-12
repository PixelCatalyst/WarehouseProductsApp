package com.pixcat.warehouseproducts;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/v1")
public class ProductController {

    private final static String ALL_IMAGES_MEDIA_TYPE = "image/*";

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;

    public ProductController(
            @Autowired ProductRepository productRepository,
            @Autowired ImageRepository imageRepository) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
    }

    @Secured("ROLE_READ_BULK_PRODUCTS")
    @GetMapping(value = "/products", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        log.info("Get all products");

        final var productDtos = productRepository.getAllProducts().stream()
                .map(ProductDto::of)
                .collect(toList());
        return ResponseEntity.ok(productDtos);
    }

    @Secured("ROLE_READ_PRODUCTS")
    @GetMapping(value = "/products/{productId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDto> getProduct(@PathVariable("productId") ProductId productId) {
        log.info("Get product {}", productId);

        final var product = productRepository.getProduct(productId);
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
        productRepository.createProduct(productId, product);

        return ResponseEntity.ok().build();
    }

    @Secured("ROLE_WRITE_PRODUCTS")
    @PutMapping(value = "/products/{productId}/image", consumes = ALL_IMAGES_MEDIA_TYPE)
    public ResponseEntity<Void> putProductImage(
            @PathVariable("productId") ProductId productId,
            @RequestHeader(value = "Content-Type") String contentType,
            InputStream image) {
        log.info("Put product {} image", productId);

        if (productRepository.containsProduct(productId)) {
            imageRepository.saveImage(productId, ImagePayload.of(image, contentType));
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
