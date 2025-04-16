CREATE TABLE IF NOT EXISTS owners (
id SERIAL primary key,
name varchar not null,
user_id int references auto_users(id)
)
