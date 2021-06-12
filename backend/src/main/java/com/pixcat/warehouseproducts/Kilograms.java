package com.pixcat.warehouseproducts;

import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode
public class Kilograms {

    public BigDecimal value;

    public Kilograms(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Weight must be present and greater than zero");
        }
        this.value = value;
    }

    public static Kilograms of(BigDecimal value) {
        return new Kilograms(value);
    }
}
