-- liquibase formatted sql

-- changeset nast:1
CREATE TABLE comments (
    pk BIGINT PRIMARY KEY NOT NULL,
    author INTEGER NOT NULL,
    author_image TEXT,
    author_first_name TEXT,
    created_at BIGINT NOT NULL,
    text TEXT NOT NULL,
    ad_id INTEGER NOT NULL
);
