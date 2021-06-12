package com.pixcat.warehouseproducts;

import java.net.URI;

public interface ImageRepository {

    URI getImageUrl(ProductId productId);

    void saveImage(ProductId productId, ImagePayload image);
}
