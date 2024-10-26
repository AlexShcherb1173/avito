-- liquibase formatted sql

-- changeset mariiam:4
ALTER TABLE users DROP CONSTRAINT users_role_fkey;
ALTER TABLE users ALTER COLUMN role TYPE VARCHAR(255);


