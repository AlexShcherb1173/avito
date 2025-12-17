-- liquibase formatted sql

-- changeset sfibikh:004
CREATE TABLE IF NOT EXISTS comments (
    id SERIAL PRIMARY KEY,
    text VARCHAR(64) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    author_id INTEGER NOT NULL,
    ad_id INTEGER NOT NULL,

    CONSTRAINT comments_author_fk FOREIGN KEY (author_id)
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT comments_ad_fk FOREIGN KEY (ad_id)
        REFERENCES ads(id) ON DELETE CASCADE
);

-- changeset sfibikh:005
CREATE INDEX comments_author_id_index ON comments (author_id);
CREATE INDEX comments_ad_id_index ON comments (ad_id);