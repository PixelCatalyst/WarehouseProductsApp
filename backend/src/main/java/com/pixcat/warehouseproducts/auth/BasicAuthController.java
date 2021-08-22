package com.pixcat.warehouseproducts.auth;

import com.pixcat.warehouseproducts.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
public class BasicAuthController {

    private final UserRepository userRepository;

    public BasicAuthController(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/auth")
    public List<String> authenticate(Principal principal) {
        log.info("Authentication attempt for {}", principal.getName());

        return userRepository
                .getUser(principal.getName())
                .getRoles();
    }
}
