-- liquibase formatted sql

-- changeset dlok:1
CREATE TABLE pictures(
    id SERIAL PRIMARY KEY,
    file_path TEXT NOT NULL,
    file_size BIGINT NOT NULL,
    media_type VARCHAR(100) NOT NULL,
    data OID NOT NULL,
    pictures_owner VARCHAR(20)
);

-- changeset dlok:2
CREATE TABLE comments(
    id SERIAL PRIMARY KEY,
    text VARCHAR(64) NOT NULL,
    created_at BIGINT NOT NULL
);

-- changeset dlok:3
CREATE TABLE ads(
    id SERIAL PRIMARY KEY,
    title VARCHAR(32) NOT NULL,
    price INTEGER NOT NULL CHECK (price >= 0),
    description VARCHAR(64) NOT NULL
);

-- changeset dlok:4
CREATE TABLE user_data (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(10) NOT NULL DEFAULT 'USER'
);

-- changeset nfr:5
ALTER TABLE pictures ADD COLUMN user_id INTEGER;
ALTER TABLE pictures ADD CONSTRAINT fk_pictures_user_data FOREIGN KEY (user_id) REFERENCES user_data(id);
ALTER TABLE pictures ADD CONSTRAINT uk_pictures_user_data UNIQUE (user_id);

-- changeset nfr:6
ALTER TABLE pictures ADD COLUMN ad_id INTEGER;
ALTER TABLE pictures ADD CONSTRAINT fk_pictures_ads FOREIGN KEY (ad_id) REFERENCES ads(id);
ALTER TABLE pictures ADD CONSTRAINT uk_pictures_ads UNIQUE (ad_id);

-- changeset nfr:7
ALTER TABLE ads ADD COLUMN user_id INTEGER;
ALTER TABLE ads ADD CONSTRAINT fk_ads_user_data FOREIGN KEY (user_id) REFERENCES user_data(id);

-- changeset nfr:8
ALTER TABLE comments ADD COLUMN ad_id INTEGER;
ALTER TABLE comments ADD CONSTRAINT fk_comments_ads FOREIGN KEY (ad_id) REFERENCES ads(id);

-- changeset nfr:9
ALTER TABLE comments ADD COLUMN user_id INTEGER;
ALTER TABLE comments ADD CONSTRAINT fk_comments_user_data FOREIGN KEY (user_id) REFERENCES user_data(id);

-- changeset dlok:10
ALTER TABLE ads ADD COLUMN picture_id INTEGER;
ALTER TABLE ads ADD CONSTRAINT fk_ads_pictures FOREIGN KEY (picture_id) REFERENCES pictures(id);
ALTER TABLE user_data ADD COLUMN picture_id INTEGER;
ALTER TABLE user_data ADD CONSTRAINT fk_user_data_pictures FOREIGN KEY (picture_id) REFERENCES pictures(id);

-- changeset dlok:11
ALTER TABLE pictures DROP CONSTRAINT uk_pictures_ads;
ALTER TABLE pictures DROP CONSTRAINT uk_pictures_user_data;