package ru.job4j.cars.service.ownershistory;

import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.model.OwnersHistory;

import java.util.Collection;
import java.util.Optional;

public interface OwnersHistoryService {
    Collection<OwnersHistory> findByCar(Car car);

    Collection<OwnersHistory> findByOwner(Owner owner);

    Optional<OwnersHistory> add(OwnersHistory ownersHistory);

    boolean deleteById(int id);

    Optional<OwnersHistory> update(OwnersHistory ownersHistory);

    Optional<OwnersHistory> findCurrentOwner();

}
