INSERT INTO users (username, first_name, last_name, password, phone, role) VALUES
('admin@mail.ru', 'admin', 'adminov', '$2a$10$PHqLP4.5DV8NXkToIWpaKehb2rKIaSaxv74hAmrBifkzWBTqJNkyS', '+7(912)345-67-89', 'ADMIN'),
('USERONE@mail.ru', 'USERONEfn', 'userov', '$2a$10$4eG1.GBPawmSx7fuMwQOE.hz7zVjr.AERz7KSCt0PuQg7iHRVpKh2', '+7(922)345-67-89', 'USER');
INSERT INTO ad (ad_text, price, title, user_id, image_url) VALUES
('descriPtion123',123,'testtest1',1,'/images/6ac38a7b-0b94-4e91-b584-e409da871aae.webp'),
('12345678',333,'456456456',1,'/images/9b32f06a-b652-43c4-bf64-53a1b64f44fa.jpg'),
('+++++++++++',666,'testtesttestuser',2,'/images/7fa28915-70f3-4a96-a794-c117902bcf71.jpg'),
('Multipart',9999999,'Multipart',2,'/images/edcaedec-a754-4f9e-b2f8-97b8f2331e57.jpeg');
INSERT INTO comment (created_at,text,ad_id,user_id) VALUES
(1734113663789,'comment 1-1',1,1),
(1734119096054,'comment 1-2',1,2),
(1734119197949,'comment 2-1',1,1),
(1734119216904,'comment 2-2',1,2),
(1734119283717,'comment 3-1',1,1),
(1734119355904,'comment 3-2',1,2),
(1734120734425,'comment 3-2',1,2);
alter sequence ad_seq restart with 100;
alter sequence comment_seq restart with 100;
alter sequence users_seq restart with 100;
INSERT INTO users (username, first_name, last_name, password, phone, role) VALUES
   ('testadmin@mail.ru', 'admin', 'adminov', '$2a$10$PHqLP4.5DV8NXkToIWpaKehb2rKIaSaxv74hAmrBifkzWBTqJNkyS', '+7(913)345-67-89', 'ADMIN'),
   ('testUSERONE@mail.ru', 'USERONEfn', 'userov', '$2a$10$4eG1.GBPawmSx7fuMwQOE.hz7zVjr.AERz7KSCt0PuQg7iHRVpKh2', '+7(923)345-67-89', 'USER');
INSERT INTO ad (ad_text, price, title, user_id, image_url) VALUES
   ('descriPtion123',123,'testtest1',1,'/images/6ac38a7b-0b94-4e91-b584-e409da871aae.webp'),
   ('12345678',333,'456456456',1,'/images/9b32f06a-b652-43c4-bf64-53a1b64f44fa.jpg'),
   ('+++++++++++',666,'testtesttestuser',2,'/images/7fa28915-70f3-4a96-a794-c117902bcf71.jpg'),
   ('Multipart',9999999,'Multipart',2,'/images/edcaedec-a754-4f9e-b2f8-97b8f2331e57.jpeg');
INSERT INTO comment (created_at,text,ad_id,user_id) VALUES
    (1734113663789,'comment 1-1',1,1),
    (1734119096054,'comment 1-2',1,2),
    (1734119197949,'comment 2-1',2,1),
    (1734119216904,'comment 2-2',2,2),
    (1734119283717,'comment 3-1',3,1),
    (1734119355904,'comment 3-2',3,2),
    (1734120734425,'comment 3-2',3,2);