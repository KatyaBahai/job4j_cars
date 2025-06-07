package ru.job4j.cars.repository.body;

import ru.job4j.cars.model.Body;

import java.util.Collection;
import java.util.Optional;

public interface BodyRepository {
    Collection<Body> findAll();

    Optional<Body> findById(int id);
}
