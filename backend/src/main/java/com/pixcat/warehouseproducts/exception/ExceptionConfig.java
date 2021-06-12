package com.pixcat.warehouseproducts.exception;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionConfig {

    @Bean
    public ResponseStatusExceptionResolver responseStatusExceptionResolver() {
        return new ResponseStatusExceptionResolver();
    }
}
