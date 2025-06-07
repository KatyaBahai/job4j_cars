create table bodies
(
    id   serial primary key,
    name varchar not null
);

ALTER TABLE cars
ADD COLUMN body_id int;

ALTER TABLE cars
ADD CONSTRAINT fk_body
FOREIGN KEY (body_id) REFERENCES bodies(id);