package ru.job4j.cars.repository.car;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class HbCarRepository implements CarRepository {
    private final CrudRepository cr;

    @Override
    public Optional<Car> add(Car car) {
        try {
            cr.run(session -> session.save(car));
            return Optional.of(car);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Collection<Car> findAllByOwnerId(int ownerId) {
        return cr.query("from Car WHERE owner_id = :fId",
                Car.class,
                Map.of("fId", ownerId));
    }

    @Override
    public Optional<Car> findById(int id) {
        return cr.optional("from Car WHERE id = :fId",
                Car.class,
                Map.of("fId", id));
    }

    @Override
    public Optional<Car> edit(Car car) {
        try {
            cr.run(session -> session.merge(car));

            return Optional.of(car);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteById(int id) {
        int affectedRows = cr.executeDeleteOrUpdate(
                "DELETE from Car e WHERE e.id = :fId", Map.of("fId", id));
        return affectedRows > 0;
    }
}
