package ru.job4j.cars.repository.engine;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class HbEngineRepository implements EngineRepository {
    private final CrudRepository cr;

    @Override
    public Collection<Engine> findAll() {
        return cr.query("from Engine", Engine.class);
    }

    @Override
    public Optional<Engine> findById(int id) {
        return cr.optional("from Engine e WHERE e.id = :fId",
                Engine.class,
                Map.of("fId", id));
    }

    @Override
    public Optional<Engine> add(Engine engine) {
        try {
            cr.run(session -> session.save(engine));
            return Optional.of(engine);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteById(int id) {
        int affectedRows = cr.executeDeleteOrUpdate(
                "DELETE from Engine e WHERE e.id = :fId", Map.of("fId", id));
        return affectedRows > 0;
    }
}
