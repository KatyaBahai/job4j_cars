package ru.job4j.cars.repository.ownershistory;

import ru.job4j.cars.model.*;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

public interface OwnersHistoryRepository {
    Collection<OwnersHistory> findByCar(Car car);
    Collection<OwnersHistory> findByOwner(Owner owner);
    Optional<OwnersHistory> add(OwnersHistory ownersHistory);

    boolean deleteById(int id);
    Optional<OwnersHistory> update(OwnersHistory ownersHistory);
    Optional<OwnersHistory> findCurrentOwner();
}
