-- liquibase formatted sql

-- changeset asemenikhin:1
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id                BIGSERIAL     PRIMARY KEY,
    email             VARCHAR   NOT NULL
                      CONSTRAINT uk_users_email UNIQUE,
    password          VARCHAR  NOT NULL,
    first_name        VARCHAR(20)   NOT NULL,
    last_name         VARCHAR(20)   NOT NULL,
    phone             VARCHAR       NOT NULL,
    role              INT DEFAULT 0 NOT NULL,
    image_path        VARCHAR       NULL
);


-- changeset asemenikhin:2
DROP TABLE IF EXISTS ads;

CREATE TABLE ads (
    id                BIGSERIAL     PRIMARY KEY,
    title             VARCHAR(32)   NOT NULL,
    price             INT           NOT NULL,
    description       VARCHAR(64)   NOT NULL,
    image_url         VARCHAR       NULL,
    user_id           BIGINT        NOT NULL,
    CONSTRAINT fk_ads_users foreign key (user_id) references users (id) ON DELETE CASCADE
);


-- changeset asemenikhin:3
DROP TABLE IF EXISTS comments;

CREATE TABLE comments (
    id                BIGSERIAL     PRIMARY KEY,
    created_at        BIGINT        NOT NULL,
    text              VARCHAR(255)  NOT NULL,
    user_id           BIGINT        NOT NULL,
    ad_id             BIGINT        NOT NULL,
    CONSTRAINT fk_comments_users foreign key (user_id) references users (id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_ads foreign key (ad_id) references ads (id) ON DELETE CASCADE
);


