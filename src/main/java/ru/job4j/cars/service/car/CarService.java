package ru.job4j.cars.service.car;

import ru.job4j.cars.model.Car;

import java.util.Collection;
import java.util.Optional;

public interface CarService {

    Optional<Car> add(Car car);

    boolean deleteById(int id);

    Optional<Car> update(Car car);

    Optional<Car> findById(int id);

    Collection<Car> findAllByOwnerId(int ownerId);
}
