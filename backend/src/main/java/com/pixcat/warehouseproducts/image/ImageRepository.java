package com.pixcat.warehouseproducts.image;

import com.pixcat.warehouseproducts.product.ProductId;

import java.net.URI;

public interface ImageRepository {

    URI getImageUrl(ProductId productId);

    void saveImage(ProductId productId, ImagePayload image);
}
