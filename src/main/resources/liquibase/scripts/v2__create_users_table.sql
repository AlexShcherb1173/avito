--liquibase formatted sql

--changeset Aidar:2
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY,
    username VARCHAR(254) NOT NULL UNIQUE,
    password VARCHAR(60),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(254) NOT NULL UNIQUE,
    phone VARCHAR(20),
    role VARCHAR(30),
    image TEXT
);

CREATE UNIQUE INDEX ux_users_username_ci ON users ((lower(username)));
CREATE UNIQUE INDEX ux_users_email_ci    ON users ((lower(email)));