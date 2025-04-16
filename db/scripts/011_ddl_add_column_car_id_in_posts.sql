ALTER TABLE auto_posts
ADD COLUMN car_id int;

ALTER TABLE auto_posts
ADD CONSTRAINT fk_car
FOREIGN KEY (car_id) REFERENCES cars(id);