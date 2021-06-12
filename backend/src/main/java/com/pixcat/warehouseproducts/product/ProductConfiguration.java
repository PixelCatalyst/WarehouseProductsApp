package com.pixcat.warehouseproducts.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class ProductConfiguration {

    @Bean
    public ProductRepository productRepository(@Autowired JdbcTemplate jdbcTemplate) {
        return new ProductRepository(jdbcTemplate);
    }
}
