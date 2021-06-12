package com.pixcat.warehouseproducts;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDto {

    private final String productId;
    private final String description;
    private final String storageTemperature;
    private final int heightInMillimeters;
    private final int widthInMillimeters;
    private final int lengthInMillimeters;
    private final BigDecimal weightInKilograms;

    public static ProductDto of(ProductDetails details) {
        return builder()
                .productId(details.getId().value)
                .description(details.getDescription())
                .storageTemperature(details.getStorageTemperature().toString())
                .heightInMillimeters(details.getHeight().value)
                .widthInMillimeters(details.getWidth().value)
                .lengthInMillimeters(details.getLength().value)
                .weightInKilograms(details.getWeight().value)
                .build();
    }
}
