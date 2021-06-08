package com.pixcat.warehouseproducts;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageConfiguration {

    @Bean ImageRepository imageRepository() {
        return new S3ImageRepository();
    }
}
