package ru.job4j.cars.service.body;

import ru.job4j.cars.model.Body;

import java.util.Collection;
import java.util.Optional;

public interface BodyService {
    Collection<Body> findAll();

    Optional<Body> findById(int id);
}
