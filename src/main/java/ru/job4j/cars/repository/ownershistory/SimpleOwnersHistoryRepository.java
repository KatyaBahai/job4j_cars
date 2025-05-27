package ru.job4j.cars.repository.ownershistory;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class SimpleOwnersHistoryRepository implements OwnersHistoryRepository {
    private CrudRepository cr;

    @Override
    public Collection<OwnersHistory> findByCar(Car car) {
        return cr.query("from OwnersHistory h WHERE h.car.id = :carId",
                OwnersHistory.class,
                Map.of("carId", car.getId()));
    }

    @Override
    public Collection<OwnersHistory> findByOwner(Owner owner) {
        return cr.query("from OwnersHistory h WHERE h.owner.id = :ownerId",
                OwnersHistory.class,
                Map.of("ownerId", owner.getId()));
    }

    @Override
    public Optional<OwnersHistory> add(OwnersHistory ownersHistory) {
        try {
            cr.run(session -> {
                session.persist(ownersHistory);
                session.flush();
            });
            return Optional.of(ownersHistory);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteById(int id) {
        return cr.executeDeleteOrUpdate(
                "from OwnersHistory WHERE id = :fId", Map.of("fId", id)) > 0;
    }

    @Override
    public Optional<OwnersHistory> update(OwnersHistory ownersHistory) {
        try {
            cr.run(session -> session.merge(ownersHistory));

            return Optional.of(ownersHistory);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<OwnersHistory> findCurrentOwner() {
        Map<String, Object> params = new HashMap<>();
        params.put("fNow", null);
        return cr.optional("from OwnersHistory WHERE endAt = :fNow",
                OwnersHistory.class, params);
    }
}
