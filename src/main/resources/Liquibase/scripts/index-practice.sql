-- liquibase formatted sql

--changeset kirill:1
create table if not exists users
(
    id           SERIAL PRIMARY KEY,
    email        VARCHAR(255) NOT NULL UNIQUE,
    first_name   VARCHAR(16)  NOT NULL,
    last_name    VARCHAR(16)  NOT NULL,
    phone        VARCHAR(20),
    role         VARCHAR(20)  NOT NULL,
    image        VARCHAR(255),
    password     VARCHAR(255) NOT NULL,
    username     VARCHAR(16)  NOT NULL UNIQUE,
    is_authorize BOOLEAN default false
);

--changeset kirill:2
CREATE TABLE IF NOT EXISTS ads
(
    id        SERIAL PRIMARY KEY,
    image     VARCHAR(255),
    price     INTEGER      NOT NULL,
    title     VARCHAR(255) NOT NULL,
    count     INTEGER,
    author_id INTEGER      NOT NULL,
    CONSTRAINT fk_ads_author FOREIGN KEY (author_id) REFERENCES users (id)
);

--changeset kirill:3
CREATE TABLE IF NOT EXISTS comments
(
    pk         SERIAL PRIMARY KEY,
    author_id  INTEGER NOT NULL,
    ad_id      INTEGER NOT NULL,
    created_at BIGINT  NOT NULL,
    text       TEXT    NOT NULL,
    CONSTRAINT fk_comment_author FOREIGN KEY (author_id) REFERENCES users (id),
    CONSTRAINT fk_comment_ad FOREIGN KEY (ad_id) REFERENCES ads (id)
);
--changeset kirill:4
CREATE TABLE IF NOT EXISTS authorities
(
    id        SERIAL PRIMARY KEY,
    username  VARCHAR(16) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    FOREIGN KEY (username) REFERENCES users (username) ON DELETE CASCADE
);

--changeset kirill:5
CREATE TABLE IF NOT EXISTS image
(
    id_image  SERIAL PRIMARY KEY,    -- Уникальный идентификатор изображения
    image_url VARCHAR(255) NOT NULL, -- URL изображения (обязательное поле)
    data      oid,                 -- Данные изображения в бинарном формате
    ad_id     INTEGER,               -- Внешний ключ на таблицу ads
    user_id   INTEGER,               -- Внешний ключ на таблицу users
    CONSTRAINT fk_image_ad FOREIGN KEY (ad_id) REFERENCES ads (id),
    CONSTRAINT fk_image_user FOREIGN KEY (user_id) REFERENCES users (id)
);
--changeset kirill:6
UPDATE DATABASECHANGELOG
SET MD5SUM = '8:70bb57eae48d77535f87f789f0fb9b95'
WHERE ID = '1' AND AUTHOR = 'kirillkrivobokov' AND FILENAME = 'classpath:/liquibase/changelog/changelog-master.yaml';
--changeset kirill:7
