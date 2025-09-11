CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_ads_author_id ON ads(author_id);
CREATE INDEX idx_comments_ad_id ON comments(ad_id);
CREATE INDEX idx_comments_author_id ON comments(author_id);

INSERT INTO users (username, password, first_name, last_name, phone, role, enabled, created_at)
VALUES ('admin@mail.ru', '{bcrypt}$2a$10$FLXrbZPHKvwk1SrGEu./uOyHj6rwCBTzWZ0KfFOPk4HszpBCU1234', 'Admin', 'User', '+79998887766', 'ADMIN', true, NOW());


INSERT INTO ads (created_at, description, image_url, price, title, author_id,) VALUES
('2024-09-05 09:06:19.132', 'Отличается милым внешним видом и жизнерадостным характером', 'image1.jpg', 145000, 'Мальтипу', 14),
('2024-08-05 19:06:19.132', 'Обладает высоким уровнем интелекта и преданностью', 'image2.jpg', 70000,'Бультерьер', 14),
('2023-09-05 19:06:19.132', 'Психически уравновешен, легко поддается дрессировке', 'image3.jpg', 90000, 'Кане-корсо',13);

INSERT INTO comments (created_at, text, ad_id, author_id) VALUES
('2024-09-05 09:06:19.132', 'Какая милая!', 33, 14),
('2024-08-05 19:06:19.132','Уточните возраст', 34, 14),
('2023-09-05 19:06:19.132', 'Когда можно забрать?', 35, 13);
