package com.pixcat.warehouseproducts;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDto {

    private final String id;
    private final String description;

    public static ProductDto of(ProductDetails details) {
        return builder()
                .id(details.getId().value)
                .description(details.getDescription())
                .build();
    }
}
