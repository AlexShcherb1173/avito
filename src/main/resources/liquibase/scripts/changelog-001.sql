--liquibase formatted sql


--changeset n.surzhikov:initdb

create table Avatar
  (
      id SERIAL primary key,
      imageDir varchar (512),
      fileType varchar (16),
      fileName varchar (32),
      fileExtension varchar (32),
      fileSize bigint

  );

create table Photo
(
    id SERIAL primary key,
    imageDir varchar (512),
    fileType varchar (16),
    fileName varchar (32),
    fileExtension varchar (32),
    fileSize bigint

);