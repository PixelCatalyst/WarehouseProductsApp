package com.pixcat.warehouseproducts.user;

import com.pixcat.warehouseproducts.user.User;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class UserRepository {

    private final static String INSERT_USER_STATEMENT =
            "INSERT INTO warehouse_products.users (username, password, enabled) VALUES (?, ?, ?)";
    private final static String INSERT_ROLE_STATEMENT =
            "INSERT INTO warehouse_products.user_authorities (username, authority) VALUES (?, ?)";
    private final static String SELECT_ALL_USERS_STATEMENT =
            "SELECT username FROM warehouse_products.users";

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addUser(User user) {
        jdbcTemplate.update(
                INSERT_USER_STATEMENT,
                user.getUsername(),
                user.getPassword(),
                user.isEnabled());

        for (String role : user.getRoles()) {
            jdbcTemplate.update(
                    INSERT_ROLE_STATEMENT,
                    user.getUsername(),
                    "ROLE_" + role);
        }
    }

    public List<String> getAllUsernames() {
        final var rows = jdbcTemplate.queryForList(SELECT_ALL_USERS_STATEMENT);
        return rows.stream()
                .map(e -> (String) e.get("username"))
                .collect(toList());
    }
}
