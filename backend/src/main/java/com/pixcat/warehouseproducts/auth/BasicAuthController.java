package com.pixcat.warehouseproducts.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicAuthController {

    @GetMapping(value = "/auth")
    public String authenticate() {
        return "User authenticated";
    }
}
