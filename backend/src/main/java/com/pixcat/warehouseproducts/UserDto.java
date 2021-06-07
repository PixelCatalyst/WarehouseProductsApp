package com.pixcat.warehouseproducts;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserDto {

    private final String username;
    private final String password;
    private final List<String> roles;
}
