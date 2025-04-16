CREATE TABLE IF NOT EXISTS owners_history (
id serial primary key,
car_id int references cars(id) not null,
owner_id int references owners(id) not null
)