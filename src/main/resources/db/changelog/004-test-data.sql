--liquibase formatted sql

-- changeset yuma:004
-- Добавляем обычного пользователя
INSERT INTO users (email, password, first_name, last_name, phone, role)
VALUES ('user@gmail.com',
        '$2a$10$9L3L9XZ8sHvFAg5xUnuF3O84R/dSl8x0vbn2dOaLrfV5CcD6L86kq', -- пароль: 1234
        'Test',
        'User',
        '89999999999',
        'USER');

-- Добавляем администратора
INSERT INTO users (email, password, first_name, last_name, phone, role)
VALUES ('admin@gmail.com',
        '$2a$10$9L3L9XZ8sHvFAg5xUnuF3O84R/dSl8x0vbn2dOaLrfV5CcD6L86kq', -- пароль: 1234
        'Admin',
        'Super',
        '89998887766',
        'ADMIN');
