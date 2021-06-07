package com.pixcat.warehouseproducts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(@Autowired UserRepository userRepository, @Autowired PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/register", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> registerUser(@RequestBody UserDto userDto) {
        final var user = User.of(userDto, passwordEncoder);
        userRepository.addUser(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/users", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getAllUsers() {
        return ResponseEntity.ok(userRepository.getAllUsernames());
    }
}
