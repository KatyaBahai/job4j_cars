create table auto_posts
(
    id   serial primary key,
    description varchar,
    creation_date timestamp not null,
    auto_user_id int references auto_users (id) not null
);
