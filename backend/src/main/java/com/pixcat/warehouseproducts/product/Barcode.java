package com.pixcat.warehouseproducts.product;


import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Barcode {

    public final String value;

    public Barcode(String value) {
        if (value == null || value.isBlank() || value.isEmpty()) {
            throw new IllegalArgumentException("Barcode string must be present and not blank");
        }
        this.value = value;
    }

    public static Barcode of(String value) {
        return new Barcode(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
