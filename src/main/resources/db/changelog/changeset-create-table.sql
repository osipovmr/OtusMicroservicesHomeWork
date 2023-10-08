--liquibase formatted sql
--changeset author:OsipovIlya

create table if not exists user_table (
id serial primary key,
username varchar(256),
first_name varchar(256),
last_name varchar(256),
email varchar(256),
phone varchar(256)
);