create table auto_users
(
    id   serial primary key,
    login varchar not null,
    password varchar not null unique
);
