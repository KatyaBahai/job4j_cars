create table auto_posts
(
    id   serial primary key,
    description varchar(256),
    creation_date timestamp not null default now(),
    auto_user_id int not null references auto_users (id),
    sold boolean default false
);
