package com.pixcat.warehouseproducts.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
@AllArgsConstructor
public class InputProductDto {

    private final String productId;
    private final String description;
    private final String storageTemperature;
    private final int heightInMillimeters;
    private final int widthInMillimeters;
    private final int lengthInMillimeters;
    private final BigDecimal weightInKilograms;
}
