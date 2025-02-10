--liquibase formatted sql

--changeset Archinski:1

CREATE TABLE public.users
(
    id         SERIAL PRIMARY KEY,
    username   varchar(255) NOT NULL UNIQUE,
    password   varchar(255) NOT NULL,
    firstname  varchar(255) NOT NULL,
    lastname   varchar(255) NOT NULL,
    phone      varchar(255) NOT NULL,
    role       varchar(255) NOT NULL,
    image      BYTEA
);

CREATE TABLE public.ads
(
    id          SERIAL PRIMARY KEY,
    title       varchar(255) NOT NULL,
    description varchar(255) NOT NULL,
    price       INTEGER NOT NULL,
    image       BYTEA,

    user_id     INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES public.users (id)
);

create table public.comments
(
    pk                INT PRIMARY KEY,
    author_id         INT NOT NULL,
    author_Image      varchar(255) NOT NULL,
    author_First_name varchar(255) NOT NULL,
    created_At        TIMESTAMP NOT NULL,
    text              varchar(255) NOT NULL,

    ad_id             INTEGER NOT NULL,
    FOREIGN KEY (ad_id) REFERENCES ads (id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES public.users (id)
);