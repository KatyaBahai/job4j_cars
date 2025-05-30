package ru.job4j.cars.repository.owner;

import ru.job4j.cars.model.Owner;

import java.util.Collection;
import java.util.Optional;

public interface OwnerRepository {
    Optional<Owner> add(Owner owner);

    Collection<Owner> findAllByCarId(int carId);

    Optional<Owner> findById(int id);

    Optional<Owner> edit(Owner owner);

    boolean deleteById(int id);
}
