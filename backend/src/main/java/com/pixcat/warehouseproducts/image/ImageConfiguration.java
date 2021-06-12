package com.pixcat.warehouseproducts.image;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageConfiguration {

    @Bean ImageRepository imageRepository() {
        return new S3ImageRepository();
    }
}
