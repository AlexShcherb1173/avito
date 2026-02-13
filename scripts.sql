CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_ads_author_id ON ads(author_id);
CREATE INDEX idx_comments_ad_id ON comments(ad_id);
CREATE INDEX idx_comments_author_id ON comments(author_id);

INSERT INTO users (email, password, first_name, last_name, phone, role) VALUES
('admin@example.com', '$2a$10$rDkPvvAFV8kqwvKJzwlRv.i.q.wz1w1pz0SFsHn/55jNeZFQvyeAy', 'Admin', 'User', '+7 (909) 123-45-67', 'ADMIN'),
('user@example.com', '$2a$10$rDkPvvAFV8kqwvKJzwlRv.i.q.wz1w1pz0SFsHn/55jNeZFQvyeAy', 'Максим', 'Анатолий', '+7 (904) 765-43-21', 'USER');

INSERT INTO ads (title, price, description, author_id) VALUES
("Мальтипу", 145000, "Отличается милым внешним видом и жизнерадостным характером", 4),
("Бультерьер", 70000, "Обладает высоким уровнем интелекта и преданностью", 2),
("Кане-корсо", 90000, "Психически уравновешен, легко поддается дрессировке", 1);

INSERT INTO comments (text, author_id, ad_id) VALUES
("Какие милые!", 1, 2, 3, 4),
("Уточните возраст", 2),
("Когда можно забрать?", 1);