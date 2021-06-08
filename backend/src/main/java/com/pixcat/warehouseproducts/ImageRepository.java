package com.pixcat.warehouseproducts;

public interface ImageRepository {

    void saveImage(ProductId productId, ImagePayload image);
}
