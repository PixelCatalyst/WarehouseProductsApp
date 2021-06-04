package com.pixcat.warehouseproducts;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        final var passwordEncoder = new BCryptPasswordEncoder();
        auth.inMemoryAuthentication()
                .withUser("john.doe").password(passwordEncoder.encode("password")).roles("GET_ALL_PRODUCTS")
                .and()
                .withUser("john.doe1").password(passwordEncoder.encode("password")).roles()
                .and()
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/v1/**").fullyAuthenticated()
                .and()
                .formLogin();
    }
}
