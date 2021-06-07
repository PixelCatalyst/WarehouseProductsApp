create schema if not exists warehouse_products;

create table if not exists warehouse_products.users
(
    username varchar(100) NOT NULL PRIMARY KEY,
    password varchar(100) NOT NULL,
    enabled boolean NOT NULL
);

create table if not exists warehouse_products.user_authorities
(
    user_role_id serial NOT NULL PRIMARY KEY,
    username varchar(100) NOT NULL,
    authority varchar(50) NOT NULL,

    UNIQUE (username, authority),
    CONSTRAINT fk_authorities
        FOREIGN KEY(username)
        REFERENCES warehouse_products.users(username)
);
