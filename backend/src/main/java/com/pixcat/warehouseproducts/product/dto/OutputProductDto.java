package com.pixcat.warehouseproducts.product.dto;

import com.pixcat.warehouseproducts.product.ProductDetails;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.net.URI;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OutputProductDto {

    private final String productId;
    private final String description;
    private final String storageTemperature;
    private final int heightInMillimeters;
    private final int widthInMillimeters;
    private final int lengthInMillimeters;
    private final BigDecimal weightInKilograms;

    private final URI imageUrl;

    public static OutputProductDto of(ProductDetails details, URI imageUrl) {
        return builder()
                .productId(details.getId().value)
                .description(details.getDescription())
                .storageTemperature(details.getStorageTemperature().toString())
                .heightInMillimeters(details.getHeight().value)
                .widthInMillimeters(details.getWidth().value)
                .lengthInMillimeters(details.getLength().value)
                .weightInKilograms(details.getWeight().value)
                .imageUrl(imageUrl)
                .build();
    }
}
