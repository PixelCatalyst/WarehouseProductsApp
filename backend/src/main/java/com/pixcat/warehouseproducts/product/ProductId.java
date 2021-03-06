package com.pixcat.warehouseproducts.product;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class ProductId {

    public final String value;

    public ProductId(String value) {
        if (value == null || value.isBlank() || value.isEmpty()) {
            throw new IllegalArgumentException("ProductId must be present and not blank");
        }
        this.value = value;
    }

    public static ProductId of(String value) {
        return new ProductId(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
