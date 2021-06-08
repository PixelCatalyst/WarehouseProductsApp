package com.pixcat.warehouseproducts;

public class S3ImageRepository implements ImageRepository {

    @Override
    public void saveImage(ProductId productId, ImagePayload image) {
        // TODO
        System.out.println("image saved");
    }
}
