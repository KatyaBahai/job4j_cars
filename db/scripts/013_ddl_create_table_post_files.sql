CREATE TABLE IF NOT EXISTS post_files (
id serial primary key,
post_id int references auto_posts(id) not null,
file_id int references files(id) not null
)