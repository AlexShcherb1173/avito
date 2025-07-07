-- changeset vkozhevatov:2
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    role VARCHAR(10) NOT NULL,
    image VARCHAR(255)
);


CREATE TABLE advertisement (
    id BIGSERIAL PRIMARY KEY,
    author_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price INTEGER NOT NULL,
    image VARCHAR(255) NOT NULL,
    CONSTRAINT fk_ad_user FOREIGN KEY (author_id) REFERENCES users(id)
);


CREATE TABLE comment (
    id BIGSERIAL PRIMARY KEY,
    author_id BIGINT NOT NULL,
    ad_id BIGINT NOT NULL,
    created_at BIGINT NOT NULL,
    text TEXT NOT NULL,
    CONSTRAINT fk_comment_user FOREIGN KEY (author_id) REFERENCES users(id),
    CONSTRAINT fk_comment_ad FOREIGN KEY (ad_id) REFERENCES advertisement(id)
);
