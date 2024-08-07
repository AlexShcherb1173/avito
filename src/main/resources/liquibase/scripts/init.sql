--liquibase formatted sql


--changeset n.surzhikov:initdb

create table "user"
(
    id SERIAL primary key,
    email varchar (32),
    password varchar (32),
    firstname varchar (32),
    lastname varchar (32),
    phone_number varchar (32),
    role varchar (32),
    image varchar (32),
    adverts varchar (32)

);

create table advert
(
    id SERIAL primary key,
    title varchar(128),
    description varchar(512),
    price decimal,
    author_id bigint references "user"(id),
    image_data bytea,
    comments varchar (512)
);
create table comments
(
    id SERIAL primary key,
    created_at timestamp,
    text varchar (512),
    author_id bigint references "user"(id),
    advert_id bigint references advert(id)


);

