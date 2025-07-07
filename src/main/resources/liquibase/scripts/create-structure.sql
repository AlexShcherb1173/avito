-- liquibase formatted sql
-- changeset vasiliy.rubtsov:1
CREATE TABLE users (
    id SERIAL NOT NULL PRIMARY KEY,	-- id пользователя
    email VARCHAR(255) NOT NULL,	-- логин пользователя
    password VARCHAR(255) NOT NULL, -- пароль пользователя
    enabled BOOLEAN NOT NULL DEFAULT TRUE, -- включен
    first_name VARCHAR(255),        -- имя пользователя
    last_name VARCHAR(255),	        -- фамилия пользователя
    phone VARCHAR(255),	            -- телефон пользователя
    role VARCHAR(255) NOT NULL,	    -- роль пользователя
    image VARCHAR(255)	            -- ссылка на аватар пользователя
);

CREATE TABLE ads (
    id SERIAL NOT NULL PRIMARY KEY,	  -- id объявления
    author_id INT NOT NULL,	          -- id автора объявления
    image varchar(255),         	  -- ссылка на картинку объявления
    price INT NOT NULL,	              -- цена объявления
    title VARCHAR(255) NOT NULL,	  -- заголовок объявления
    description VARCHAR(255) NOT NULL -- описание объявления
);

CREATE TABLE comments (
    id SERIAL NOT NULL PRIMARY KEY,	 -- id комментария
    author_id INT NOT NULL,	         -- id автора комментария
    ad_id INT NOT NULL,              -- id объявления
    created_at TIMESTAMP NOT NULL,	 -- дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970
    text VARCHAR(255) NOT NULL	     -- текст комментария
);

ALTER TABLE ads ADD FOREIGN KEY (author_id) REFERENCES users(id);
ALTER TABLE comments ADD FOREIGN KEY (author_id) REFERENCES users(id);
ALTER TABLE comments ADD FOREIGN KEY (ad_id) REFERENCES ads(id);

CREATE INDEX ix__comments__author_id ON comments(author_id);
CREATE INDEX ix__comments__ad_id ON comments(ad_id);
CREATE INDEX ix__ads__author_id ON ads(author_id);
CREATE INDEX ix__users__email ON users(email);
