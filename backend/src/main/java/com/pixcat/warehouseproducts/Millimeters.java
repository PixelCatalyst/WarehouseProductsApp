package com.pixcat.warehouseproducts;


import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Millimeters {

    public int value;

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
