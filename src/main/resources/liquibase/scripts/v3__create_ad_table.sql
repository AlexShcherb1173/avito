--liquibase formatted sql

--changeset artem:3

CREATE TABLE IF NOT EXISTS ad (
    id BIGINT PRIMARY KEY,
    description VARCHAR(1000) NOT NULL CHECK (length(description) >= 10),
    image VARCHAR(255) NOT NULL,
    price NUMERIC NOT NULL CHECK (price >= 0),
    title VARCHAR(100) NOT NULL CHECK (length(title) >= 3),
    users_id BIGINT NOT NULL
);