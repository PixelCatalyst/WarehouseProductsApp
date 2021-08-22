package com.pixcat.warehouseproducts.user;

import kotlin.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;

public class UserRepository {

    private final static String INSERT_USER_STATEMENT =
            "INSERT INTO warehouse_products.users (username, password, enabled) VALUES (?, ?, ?)";
    private final static String INSERT_ROLE_STATEMENT =
            "INSERT INTO warehouse_products.user_authorities (username, authority) VALUES (?, ?)";
    private final static String SELECT_ALL_USERS_STATEMENT =
            "SELECT username FROM warehouse_products.users";
    private final static String SELECT_USER_STATEMENT = """
            SELECT users.username, users.password, auth.authority
            FROM warehouse_products.users users
                INNER JOIN warehouse_products.user_authorities auth
                ON users.username = auth.username
            WHERE users.username = ?
            """;

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

    public UserDto getUser(String username) {
        final var rows = jdbcTemplate.queryForList(SELECT_USER_STATEMENT, username);
        final var maybeUser = rows.stream()
                .collect(groupingBy(r -> new Pair<>(
                        (String) r.get("username"),
                        (String) r.get("password")))
                )
                .entrySet().stream()
                .findFirst()
                .map(user -> new UserDto(
                        user.getKey().getFirst(),
                        user.getKey().getSecond(),
                        user.getValue().stream()
                                .map(authorities -> (String) authorities.get("authority"))
                                .collect(toUnmodifiableList()))
                );
        return maybeUser.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<String> getAllUsernames() {
        final var rows = jdbcTemplate.queryForList(SELECT_ALL_USERS_STATEMENT);
        return rows.stream()
                .map(e -> (String) e.get("username"))
                .collect(toList());
    }
}
