CREATE TABLE ads (
    id SERIAL PRIMARY KEY,
    title VARCHAR(32) NOT NULL,
    price INTEGER NOT NULL CHECK (price >= 0 AND price <= 10000000),
    description VARCHAR(64),
    image_url TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    author_id INTEGER NOT NULL,
    CONSTRAINT fk_ads_author FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);