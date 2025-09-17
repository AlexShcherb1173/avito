--liquibase formatted sql

--changeset forzik6k:5
ALTER TABLE comment
    ALTER COLUMN ad_id SET NOT NULL,
    ADD CONSTRAINT fk_comment_ad FOREIGN KEY (ad_id) REFERENCES ad(id) ON DELETE RESTRICT;
CREATE INDEX idx_comment_ad_id ON comment(ad_id);