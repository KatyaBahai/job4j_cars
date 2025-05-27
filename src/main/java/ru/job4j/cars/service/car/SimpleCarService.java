package ru.job4j.cars.service.car;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.repository.car.CarRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimpleCarService implements CarService {
    private final CarRepository carRepository;

    @Override
    public Optional<Car> add(Car car) {
        return carRepository.add(car);
    }

    @Override
    public boolean deleteById(int id) {
        return carRepository.deleteById(id);
    }

    @Override
    public Optional<Car> update(Car car) {
        return carRepository.edit(car);
    }

    @Override
    public Optional<Car> findById(int id) {
        return carRepository.findById(id);
    }

    @Override
    public Collection<Car> findAllByOwnerId(int ownerId) {
        return carRepository.findAllByOwnerId(ownerId);
    }
}
