package ru.job4j.cars.service.owner;

import ru.job4j.cars.model.Owner;

import java.util.Collection;
import java.util.Optional;

public interface OwnerService {

    Optional<Owner> save(Owner owner);

    boolean deleteById(int id);

    Optional<Owner> update(Owner owner);

    Optional<Owner> findById(int id);

    Collection<Owner> findAllByCarId(int carId);

}
