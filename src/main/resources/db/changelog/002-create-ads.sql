--liquibase formatted sql

-- changeset yuma:002
CREATE TABLE ads (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(10,2),
    image VARCHAR(255),
    author_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);