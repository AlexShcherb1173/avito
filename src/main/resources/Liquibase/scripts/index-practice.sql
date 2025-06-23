-- liquibase formatted sql

--changeset kirill:1
create table if not exists users_profiles
(
    id           SERIAL PRIMARY KEY,
    email        VARCHAR(255) NOT NULL UNIQUE,
    first_name   VARCHAR(16)  NOT NULL,
    last_name    VARCHAR(16)  NOT NULL,
    phone        VARCHAR(20),
    role         VARCHAR(20)  NOT NULL,
    image        VARCHAR(255),
    password     VARCHAR(255) NOT NULL,
    is_authorize BOOLEAN default false
);


CREATE TABLE IF NOT EXISTS ads
(
    id        SERIAL PRIMARY KEY,
    image     VARCHAR(255),
    price     INTEGER      NOT NULL,
    title     VARCHAR(255) NOT NULL,
    count     INTEGER,
    author_id INTEGER      NOT NULL,
    CONSTRAINT fk_ads_author FOREIGN KEY (author_id) REFERENCES users_profiles (id)
);


CREATE TABLE IF NOT EXISTS comments
(
    pk         SERIAL PRIMARY KEY,
    author_id  INTEGER NOT NULL,
    ad_id      INTEGER NOT NULL,
    created_at BIGINT  NOT NULL,
    text       TEXT    NOT NULL,
    CONSTRAINT fk_comment_author FOREIGN KEY (author_id) REFERENCES users_profiles (id),
    CONSTRAINT fk_comment_ad FOREIGN KEY (ad_id) REFERENCES ads (id)
);

CREATE TABLE IF NOT EXISTS image
(
    id_image  SERIAL PRIMARY KEY,
    image_url VARCHAR(255) NOT NULL,
    data      oid,
    ad_id     INTEGER,
    user_id   INTEGER,
    CONSTRAINT fk_image_ad FOREIGN KEY (ad_id) REFERENCES ads (id),
    CONSTRAINT fk_image_user FOREIGN KEY (user_id) REFERENCES users_profiles (id)
);

CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) NOT NULL PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    enabled BOOLEAN NOT NULL
    );
CREATE TABLE IF NOT EXISTS authorities
(
    id        SERIAL PRIMARY KEY,
    username  VARCHAR(16) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    FOREIGN KEY (username) REFERENCES users (username) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS authorities (
                                             username VARCHAR(50) NOT NULL,
                                             authority VARCHAR(50) NOT NULL,
                                             FOREIGN KEY (username) REFERENCES users(username)
  );
