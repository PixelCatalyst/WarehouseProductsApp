package com.pixcat.warehouseproducts.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class UserConfiguration {

    @Bean
    public UserRepository userRepository(@Autowired JdbcTemplate jdbcTemplate) {
        return new UserRepository(jdbcTemplate);
    }
}
