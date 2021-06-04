package com.pixcat.warehouseproducts;

import lombok.Value;

@Value(staticConstructor = "of")
public class ProductId {

    public String value;

    @Override
    public String toString() {
        return value;
    }
}
