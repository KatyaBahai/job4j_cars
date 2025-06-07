CREATE TABLE IF NOT EXISTS cars (
id SERIAL primary key,
name varchar,
engine_id int references engines(id) not null,
owner_id int references owners(id) not null
)
