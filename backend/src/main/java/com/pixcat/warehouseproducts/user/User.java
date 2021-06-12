package com.pixcat.warehouseproducts.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    private final String username;
    private final String password;
    private final boolean enabled;
    private final List<String> roles;

    public static User of(UserDto dto, PasswordEncoder passwordEncoder) {
        return builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .enabled(true)
                .roles(List.copyOf(dto.getRoles()))
                .build();
    }
}
