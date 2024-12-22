-- liquibase formated sql
-- changeset norekhov:1
create sequence ad_seq
    increment by 50;

alter sequence ad_seq owner to "user";

create sequence comment_seq
    increment by 50;

alter sequence comment_seq owner to "user";

create sequence users_seq
    increment by 50;

alter sequence users_seq owner to "user";

create table if not exists users
(
    enabled    integer,
    user_id    integer not null primary key,
    avatar     varchar(255),
    first_name varchar(255),
    last_name  varchar(255),
    password   varchar(255),
    phone      varchar(255),
    role       varchar(255) default USER
        constraint users_role_check
            check ((role)::text = ANY ((ARRAY ['USER'::character varying, 'ADMIN'::character varying])::text[])),
    username   varchar(255)
);

alter table users
    owner to "user";

create table if not exists ad
(
    ad_id     integer not null
        primary key,
    price     integer,
    user_id   integer
        constraint fktofr7l4mo2aajd4mm1k7n93a6
            references users,
    ad_text   varchar(255),
    image_url varchar(255),
    title     varchar(255)
);

alter table ad
    owner to "user";

create table if not exists comment
(
    ad_id      integer
        constraint fkc5at2qnh29gd8s96s19qc63wu
            references ad,
    comment_id integer not null
        primary key,
    user_id    integer
        constraint fkqm52p1v3o13hy268he0wcngr5
            references users,
    created_at bigint,
    text       varchar(255)
);

alter table comment
    owner to "user";