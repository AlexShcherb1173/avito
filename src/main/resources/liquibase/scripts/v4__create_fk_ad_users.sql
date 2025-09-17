--liquibase formatted sql

--changeset frozik6k:4
ALTER TABLE ad
    ALTER COLUMN users_id SET NOT NULL,
    ADD CONSTRAINT fk_ad_users FOREIGN KEY (users_id) REFERENCES users(id) ON DELETE RESTRICT;
CREATE INDEX idx_ad_users_id ON ad(users_id);