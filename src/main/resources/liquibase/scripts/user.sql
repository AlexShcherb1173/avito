-- liquibase formatted sql

-- changelog user2:1
create table Comment(
id bigint,
author int not null ,
authorImage text,
authorFirstName text,
createdAt int,
pk int,
foreign key (author) references announcements(author)
)
-- changelog user2:2
create table Announcements(
id bigint,
author int not null ,
image text,
pk int,
price int,
title text,
foreign key (author) references "User"(firstname)
)
-- changelog user2:3
create table "User"(
id bigint,
email text,
firstName text not null ,
lastName text,
phone text,
role text,
image text,
password text not null,
foreign key ("password") references announcements
)










