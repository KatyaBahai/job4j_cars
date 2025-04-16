package ru.job4j.cars.repository.owner;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class HbOwnerRepository implements OwnerRepository {
    private final CrudRepository cr;

    @Override
    public Optional<Owner> add(Owner owner) {
        try {
            cr.run(session -> session.save(owner));
            return Optional.of(owner);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Collection<Owner> findAllByCarId(int carId) {
        return cr.query("from Owner WHERE owner_id = :fId",
                Owner.class,
                Map.of("fId", carId));
    }

    @Override
    public Optional<Owner> findById(int id) {
        return cr.optional("from Owner WHERE id = :fId",
                Owner.class,
                Map.of("fId", id));
    }

    @Override
    public Optional<Owner> edit(Owner owner) {
        try {
            cr.run(session -> session.merge(owner));

            return Optional.of(owner);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteById(int id) {
        int affectedRows = cr.executeDeleteOrUpdate(
                "DELETE from Owner WHERE id = :fId", Map.of("fId", id));
        return affectedRows > 0;
    }
}
