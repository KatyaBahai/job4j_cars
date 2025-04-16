package ru.job4j.cars.repository.car;

import ru.job4j.cars.model.Car;

import java.util.Collection;
import java.util.Optional;

public interface CarRepository {
    Optional<Car> add(Car car);

    Collection<Car> findAllByOwnerId(int ownerId);

    Optional<Car> findById(int id);

    Optional<Car> edit(Car car);

    boolean deleteById(int id);
}
