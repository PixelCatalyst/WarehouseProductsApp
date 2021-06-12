package com.pixcat.warehouseproducts.product;

import com.pixcat.warehouseproducts.image.ImagePayload;
import com.pixcat.warehouseproducts.image.ImageRepository;
import com.pixcat.warehouseproducts.product.dto.InputProductDto;
import com.pixcat.warehouseproducts.product.dto.OutputProductDto;
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
    public ResponseEntity<List<OutputProductDto>> getAllProducts() {
        log.info("Get all products");

        final var productDtos = productRepository.getAllProducts().stream()
                .map(product -> OutputProductDto.of(
                        product,
                        imageRepository.getImageUrl(product.getId())
                ))
                .collect(toList());
        return ResponseEntity.ok(productDtos);
    }

    @Secured("ROLE_READ_PRODUCTS")
    @GetMapping(value = "/products/{productId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<OutputProductDto> getProductById(@PathVariable("productId") ProductId productId) {
        log.info("Get product {}", productId);

        final var product = productRepository.getProductById(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        final var productImage = imageRepository.getImageUrl(product.getId());
        return ResponseEntity.ok(OutputProductDto.of(product, productImage));
    }

    @Secured("ROLE_READ_PRODUCTS")
    @GetMapping(value = "/barcodes/{barcode}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<OutputProductDto> getProductByBarcode(@PathVariable("barcode") Barcode barcode) {
        log.info("Get product by barcode {}", barcode);

        final var product = productRepository.getProductByBarcode(barcode);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        final var productImage = imageRepository.getImageUrl(product.getId());
        return ResponseEntity.ok(OutputProductDto.of(product, productImage));
    }

    @Secured("ROLE_WRITE_PRODUCTS")
    @PutMapping(value = "/products/{productId}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> putProduct(
            @PathVariable("productId") ProductId productId,
            @RequestBody InputProductDto productDto) {
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
