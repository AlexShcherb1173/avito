-- Создание таблицы owners
CREATE TABLE owners (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(20),
    enabled BOOLEAN NOT NULL DEFAULT true,
    role VARCHAR(50)
);

-- Создание таблицы users
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    enabled BOOLEAN NOT NULL DEFAULT true,
    first_name VARCHAR(255),
    image VARCHAR(255),
    last_name VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(50),
    username VARCHAR(255) NOT NULL UNIQUE,
    owner_id INTEGER NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES owners(id) ON DELETE CASCADE
);

-- Создание таблицы ads
CREATE TABLE ads (
    pk SERIAL PRIMARY KEY,
    author INTEGER NOT NULL,
    author_first_name VARCHAR(255),
    author_last_name VARCHAR(255),
    description VARCHAR(255),
    email VARCHAR(255),
    image VARCHAR(255),
    phone VARCHAR(20),
    price INTEGER,
    title VARCHAR(255)
);

-- Создание таблицы ad_model
CREATE TABLE ad_model (
    ad_id SERIAL PRIMARY KEY,
    author INTEGER NOT NULL,
    description VARCHAR(255),
    image VARCHAR(255),
    image_update_path VARCHAR(255),
    price INTEGER,
    title VARCHAR(255),
    ad_image_id INTEGER,
    user_id INTEGER,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Создание таблицы comments
CREATE TABLE comments (
    pk SERIAL PRIMARY KEY,
    author INTEGER NOT NULL,
    author_first_name VARCHAR(255),
    author_image VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    text VARCHAR(255)
);

-- Создание таблицы comment_model
CREATE TABLE comment_model (
    comment_id SERIAL PRIMARY KEY,
    author INTEGER NOT NULL,
    author_first_name VARCHAR(255),
    author_image VARCHAR(255),
    create_ad INTEGER,
    text VARCHAR(255),
    ad_id INTEGER NOT NULL,
    user_id INTEGER,
    FOREIGN KEY (ad_id) REFERENCES ads(pk) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Создание таблицы app_user
CREATE TABLE app_user (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    image VARCHAR(255),
    last_name VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(50),
    username VARCHAR(255) NOT NULL UNIQUE,
    user_image_id INTEGER
);

-- Создание таблицы authorities
CREATE TABLE authorities (
    username VARCHAR(255) NOT NULL,
    authority VARCHAR(255) NOT NULL,
    FOREIGN KEY (username) REFERENCES app_user(username) ON DELETE CASCADE
);

-- Создание таблицы image_model
CREATE TABLE image_model (
    image_id SERIAL PRIMARY KEY,
    data OID,
    file_path VARCHAR(255),
    file_size INTEGER,
    media_type VARCHAR(50)
);

-- Создание таблицы login
CREATE TABLE login (
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);