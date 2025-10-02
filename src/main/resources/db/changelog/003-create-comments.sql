--liquibase formatted sql

-- changeset yuma:003
CREATE TABLE comments (
    id SERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT now(),
    author_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    ad_id INT NOT NULL REFERENCES ads(id) ON DELETE CASCADE
);