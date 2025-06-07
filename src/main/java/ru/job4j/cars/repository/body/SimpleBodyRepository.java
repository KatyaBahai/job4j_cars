package ru.job4j.cars.repository.body;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Body;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class SimpleBodyRepository implements BodyRepository {
    private final CrudRepository cr;

    @Override
    public Collection<Body> findAll() {
        return cr.query("from Body", Body.class);
    }

    @Override
    public Optional<Body> findById(int id) {
        return cr.optional("from Body b WHERE b.id = :fId",
                Body.class,
                Map.of("fId", id));
    }
}
