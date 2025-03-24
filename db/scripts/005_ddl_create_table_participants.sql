CREATE TABLE IF NOT EXISTS participants (
id serial PRIMARY KEY,
user_id int not null references auto_users(id),
post_id int not null references auto_posts(id),
UNIQUE (post_id, user_id)
);