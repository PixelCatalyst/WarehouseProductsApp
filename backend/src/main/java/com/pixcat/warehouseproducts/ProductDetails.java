package com.pixcat.warehouseproducts;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductDetails {

    private final ProductId id;
    private final String description;

    public static ProductDetails of(ProductDto dto) {
        return builder()
                .id(ProductId.of(dto.getId()))
                .description(dto.getDescription())
                .build();
    }
}
