-- Таблицы, созданные Hibernate
-- liquibase formatted sql

-- changeset asemenikhin:1
DROP TABLE IF EXISTS users;

CREATE TABLE users (
	id                 BIGSERIAL      PRIMARY KEY,
	email              VARCHAR(32)    NOT NULL,
	password           VARCHAR(16)    NOT NULL,
	first_name         VARCHAR(10)    NOT NULL,
	last_name          VARCHAR(10)    NOT NULL,
	image_path         VARCHAR(255)   NULL,
	phone              VARCHAR(255)   NOT NULL,
	role               INT DEFAULT 0  NOT NULL,
	CONSTRAINT uk_users_email UNIQUE (email)
);


-- changeset asemenikhin:2
DROP TABLE IF EXISTS ads;

CREATE TABLE ads (
    id                 BIGSERIAL      PRIMARY KEY,
    title              VARCHAR(32)    NOT NULL,
    price              INT            NOT NULL,
	description        VARCHAR(64)    NOT NULL,
	image_url          VARCHAR(255)   NULL,
	user_id            BIGINT         NOT NULL,
	CONSTRAINT fk_ads_users FOREIGN KEY (user_id) REFERENCES users(id)
);


-- changeset asemenikhin:3
DROP TABLE IF EXISTS comments;

CREATE TABLE comments (
    id                BIGSERIAL       PRIMARY KEY,
	created_at        BIGINT          NOT NULL,
	text              VARCHAR(255)    NOT NULL,
	user_id           BIGINT          NOT NULL,
	ad_id             BIGINT          NOT NULL,
	CONSTRAINT fk_comments_users FOREIGN KEY (user_id) REFERENCES users(id),
	CONSTRAINT fk_comments_ads FOREIGN KEY (ad_id) REFERENCES ads(id)
);