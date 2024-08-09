--liquibase formatted sql


--changeset n.surzhikov:initdb

CREATE TABLE images (
    id BIGSERIAL PRIMARY KEY,
    file_size BIGINT NOT NULL,
    media_type VARCHAR(255),
    data BYTEA
);

-- Затем создаем таблицу для пользователей
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    phone_number VARCHAR(255),
    role VARCHAR(50),
    image BIGINT,
    enabled BOOLEAN NOT NULL,
    FOREIGN KEY (image) REFERENCES images(id)
);

-- После этого создаем таблицу для объявлений
CREATE TABLE advert (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    description VARCHAR(64) NOT NULL,
    author_id BIGINT NOT NULL,
    image BIGINT,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (image) REFERENCES images(id)
);

-- Если нужно, создаем таблицу для комментариев
CREATE TABLE comments (
    id BIGSERIAL PRIMARY KEY,
    advert_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    FOREIGN KEY (advert_id) REFERENCES advert(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE authorities (
    username VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT authorities_pkey PRIMARY KEY (username, authority)
);