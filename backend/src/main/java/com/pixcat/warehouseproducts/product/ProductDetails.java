package com.pixcat.warehouseproducts.product;

import com.pixcat.warehouseproducts.product.dto.InputProductDto;
import com.pixcat.warehouseproducts.type.Kilograms;
import com.pixcat.warehouseproducts.type.Millimeters;
import com.pixcat.warehouseproducts.type.Temperature;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductDetails {

    private final ProductId id;
    private final String description;
    private final Temperature storageTemperature;
    private final Millimeters height;
    private final Millimeters width;
    private final Millimeters length;
    private final Kilograms weight;

    public static ProductDetails of(InputProductDto dto) {
        return builder()
                .id(ProductId.of(dto.getProductId()))
                .description(dto.getDescription())
                .storageTemperature(Temperature.valueOf(dto.getStorageTemperature()))
                .height(Millimeters.of(dto.getHeightInMillimeters()))
                .width(Millimeters.of(dto.getWidthInMillimeters()))
                .length(Millimeters.of(dto.getLengthInMillimeters()))
                .weight(Kilograms.of(dto.getWeightInKilograms()))
                .build();
    }
}
