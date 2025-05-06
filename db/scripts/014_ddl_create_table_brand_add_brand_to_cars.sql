create table brands
(
    id   serial primary key,
    name varchar not null
);

ALTER TABLE cars
ADD COLUMN brand_id int;

ALTER TABLE cars
ADD CONSTRAINT fk_brand
FOREIGN KEY (brand_id) REFERENCES brands(id);