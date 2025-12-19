-- Создание тестового пользователя с ролью ADMIN
INSERT INTO users (email, first_name, last_name, phone, role, password, image)
SELECT 'admin@gmail.com', 'Admin', 'User', '+7 (123) 456-78-90', 'ADMIN', '$2a$10$rDkPvvAFV8kqwvKJzwlRv.i.q.wl1G.0AeOjYpCconlym4SOV.5Qa', 'admin_avatar.jpg'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@gmail.com');

-- Создание тестового пользователя с ролью USER
INSERT INTO users (email, first_name, last_name, phone, role, password, image)
SELECT 'user@gmail.com', 'Regular', 'User', '+7 (987) 654-32-10', 'USER', '$2a$10$rDkPvvAFV8kqwvKJzwlRv.i.q.wl1G.0AeOjYpCconlym4SOV.5Qa', 'user_avatar.jpg'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'user@gmail.com');