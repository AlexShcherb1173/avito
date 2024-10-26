-- liquibase formatted sql

-- changeset mariiam:1
CREATE TABLE IF NOT EXISTS roles (
  id bigserial PRIMARY KEY,
  title VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS users (
  id bigserial PRIMARY KEY,
  f_name VARCHAR(255) NOT NULL,
  s_name VARCHAR(255) NOT NULL,
  l_name VARCHAR(255),
  login VARCHAR(255) NOT NULL,
  phone VARCHAR(255),
  reg_date timestamp NOT NULL,
  email VARCHAR(255),
  "role" INT4 NOT NULL REFERENCES roles (id) ON DELETE CASCADE,
  "password" VARCHAR(255) NOT NULL,
  image BYTEA
);

CREATE TABLE IF NOT EXISTS ads (
  id bigserial PRIMARY KEY,
  author INT8 NOT NULL REFERENCES users (id) ON DELETE CASCADE,
  image BYTEA,
  price INT4 NOT NULL,
  title VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS "comments" (
  id bigserial PRIMARY KEY,
  author INT8 NOT NULL REFERENCES users (id) ON DELETE CASCADE,
  created_at  TIMESTAMP NOT NULL,
  "text" VARCHAR(255) NOT NULL
);

-- changeset yuriy-kolosov:2
ALTER TABLE ads ALTER COLUMN image TYPE VARCHAR(255);

ALTER TABLE ads ALTER COLUMN price TYPE DECIMAL(8,2);

ALTER TABLE users ALTER COLUMN image TYPE VARCHAR(255);

-- changeset yuriy-kolosov:3
ALTER TABLE ads ALTER COLUMN price SET NOT NULL;


