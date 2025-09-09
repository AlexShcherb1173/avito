CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(32) UNIQUE NOT NULL,
    password TEXT NOT NULL,
    first_name VARCHAR(16) NOT NULL,
    last_name VARCHAR(16) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(10) NOT NULL CHECK (role IN ('USER', 'ADMIN')),
    image_url TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);