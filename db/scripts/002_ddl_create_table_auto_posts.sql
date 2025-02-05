create table auto_posts
(
    id   serial primary key,
    description varchar not null,
    creation_date varchar not null unique,
    auto_user_id int references auto_users (id) not null
);
