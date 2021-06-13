create schema if not exists warehouse_products;

--- Users ---

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

--- Products ---

create table if not exists warehouse_products.details
(
    product_id varchar(100) NOT NULL PRIMARY KEY,
    description varchar(150),
    storage_temp varchar(50) NOT NULL,
    height smallint NOT NULL,
    width smallint NOT NULL,
    length smallint NOT NULL,
    weight numeric NOT NULL,

    CONSTRAINT measures_nonnegative CHECK (height > 0 AND width > 0 AND length > 0),
    CONSTRAINT weight_nonnegative CHECK (weight > 0)
);

create table if not exists warehouse_products.barcodes
(
    barcode varchar(100) NOT NULL PRIMARY KEY,
    product_id varchar(100) NOT NULL,

    CONSTRAINT fk_barcodes
        FOREIGN KEY(product_id)
        REFERENCES warehouse_products.details(product_id)
);
