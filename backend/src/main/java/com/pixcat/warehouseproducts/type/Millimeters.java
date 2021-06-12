package com.pixcat.warehouseproducts.type;


import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Millimeters {

    public final int value;

    public Millimeters(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Physical dimension must be greater than zero");
        }

        this.value = value;
    }

    public static Millimeters of(int value) {
        return new Millimeters(value);
    }
}
