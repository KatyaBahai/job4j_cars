create table auto_users
(
    id serial primary key,
    login varchar not null unique,
    password varchar not null
);
