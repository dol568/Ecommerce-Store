DROP TABLE if EXISTS `role`;
DROP TABLE if EXISTS cart_item;
DROP TABLE if EXISTS order_item;
DROP TABLE if EXISTS `order`;
DROP TABLE if EXISTS address;
DROP TABLE if EXISTS `user`;
DROP TABLE if EXISTS cart;
DROP TABLE if EXISTS product;
DROP TABLE if EXISTS product_brand;
DROP TABLE if EXISTS product_type;
DROP TABLE if EXISTS users_roles;

CREATE TABLE role
(
    id   bigint      not null auto_increment,
    name varchar(40) not null,
    primary key (id)
);

CREATE TABLE users_roles
(
    role_id bigint not null,
    user_id bigint not null,
    primary key (role_id, user_id)
);

CREATE TABLE user
(
    is_enabled    bit          not null,
    is_non_locked bit          not null,
    id            bigint       not null auto_increment,
    username      varchar(45)  not null,
    password      varchar(64)  not null,
    email         varchar(125) not null,
    primary key (id)
);

CREATE TABLE address
(
    id          bigint not null auto_increment,
    user_id     bigint,
    city        varchar(255),
    country     varchar(255),
    name        varchar(255),
    postal_code varchar(255),
    province    varchar(255),
    street      varchar(255),
    primary key (id)
);

CREATE TABLE cart
(
    total       decimal(38, 2),
    total_items integer,
    id          bigint not null auto_increment,
    primary key (id)
);

CREATE TABLE cart_item
(
    quantity   integer,
    cart_id    bigint,
    id         bigint not null auto_increment,
    user_id    bigint,
    product_id varchar(255),
    primary key (id)
);

CREATE TABLE `order`
(
    total               decimal(38, 2),
    order_date          datetime(6),
    shipping_address_id bigint,
    buyer_email         varchar(255),
    id                  varchar(255) not null,
    order_status        enum ('DELIVERED','PENDING','SHIPPED'),
    primary key (id)
);

CREATE TABLE order_item
(
    quantity        integer,
    unit_price      decimal(38, 2),
    id              bigint not null auto_increment,
    order_id        varchar(255),
    picture_url     varchar(255),
    product_item_id varchar(255),
    product_name    varchar(255),
    primary key (id)
);

CREATE TABLE product
(
    unit_price  decimal(38, 2),
    brand_id    varchar(255) not null,
    description varchar(255),
    id          varchar(255) not null,
    name        varchar(255),
    picture_url varchar(255),
    type_id     varchar(255) not null,
    primary key (id)
);

CREATE TABLE product_brand
(
    id   varchar(255) not null,
    name varchar(255),
    primary key (id)
);

CREATE TABLE product_type
(
    id   varchar(255) not null,
    name varchar(255),
    primary key (id)
);