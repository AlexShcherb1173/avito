-- liquibase formatted sql

-- changeset v.aliyev:1
CREATE TABLE IF NOT EXISTS public.ads
(
    id integer PRIMARY KEY,
    description character varying(1024),
    price integer NOT NULL,
    title character varying(255),
    user_id integer,
    image_id integer
);
CREATE TABLE IF NOT EXISTS public.images
(
    id integer PRIMARY KEY,
    data oid,
    file_size bigint NOT NULL,
    media_type character varying(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS public.comments
(
     pk integer PRIMARY KEY,
     text character varying(512) NOT NULL,
     createdAt bigint NOT NULL,
     author_id integer NOT NULL,
     ad_id integer NOT NULL
);
CREATE TABLE IF NOT EXISTS public.users
(
     id integer PRIMARY KEY,
     username character varying(255) NOT NULL,
     firstName character varying(255) NOT NULL,
     lastName character varying(255) NOT NULL,
     password character varying(255) NOT NULL,
     phone character varying(30) NOT NULL,
     role character varying(255) NOT NULL,
     image_id integer NOT NULL
);