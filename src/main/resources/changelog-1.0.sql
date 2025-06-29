-- changeset vkozhevatov:1
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(10) NOT NULL
);


CREATE TABLE ads (
    id BIGSERIAL PRIMARY KEY,
    author_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price INTEGER NOT NULL,
    image_path VARCHAR(255) NOT NULL,
    CONSTRAINT fk_ad_user FOREIGN KEY (author_id) REFERENCES users(id)
);


CREATE TABLE comments (
    id BIGSERIAL PRIMARY KEY,
    author_id BIGINT NOT NULL,
    ad_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    text TEXT NOT NULL,
    CONSTRAINT fk_comment_user FOREIGN KEY (author_id) REFERENCES users(id),
    CONSTRAINT fk_comment_ad FOREIGN KEY (ad_id) REFERENCES ads(id)
);
