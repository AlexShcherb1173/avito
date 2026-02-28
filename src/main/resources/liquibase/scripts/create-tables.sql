-- liquibase formatted sql

-- changeset Tim:1

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR NOT NULL UNIQUE,
    first_name VARCHAR NOT NULL,
    last_name VARCHAR NOT NULL,
    phone VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    role VARCHAR NOT NULL,
    image VARCHAR
);

CREATE TABLE ads (
    pk SERIAL PRIMARY KEY,
    author_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR NOT NULL,
    price INTEGER NOT NULL,
    description VARCHAR NOT NULL,
    image VARCHAR
);

CREATE TABLE comments (
    pk SERIAL PRIMARY KEY,
    author_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    ad_id INTEGER NOT NULL REFERENCES ads(pk) ON DELETE CASCADE,
    text VARCHAR NOT NULL,
    created_at BIGINT NOT NULL
);