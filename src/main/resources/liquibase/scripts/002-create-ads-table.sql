-- liquibase formatted sql

-- changeset sfibikh:002
CREATE TABLE IF NOT EXISTS ads (
    id SERIAL PRIMARY KEY,
    title VARCHAR(32) NOT NULL,
    price INTEGER NOT NULL CHECK (price >= 0),
    description VARCHAR(64) NOT NULL,
    image VARCHAR(500),
    author_id INTEGER NOT NULL,

    CONSTRAINT ads_author_fk FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);

-- changeset sfibikh:003
CREATE INDEX ads_author_id_index ON ads (author_id);