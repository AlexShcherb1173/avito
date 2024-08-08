--liquibase formatted sql


--changeset n.surzhikov:initdb

CREATE TABLE users
(
    id SERIAL PRIMARY KEY,
    username VARCHAR(32) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    firstname VARCHAR(32),
    lastname VARCHAR(32),
    phone_number VARCHAR(32),
    role VARCHAR(32),
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE authorities (
    username VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT authorities_pkey PRIMARY KEY (username, authority)
);

create table advert
(
    id SERIAL primary key,
    title varchar(128),
    description varchar(512),
    price decimal,
    author_id bigint references "users"(id),
    image bytea,
    comments varchar (512)
);
create table comments
(
    id SERIAL primary key,
    created_at timestamp,
    text varchar (512),
    author_id bigint references "users"(id),
    advert_id bigint references advert(id)


);