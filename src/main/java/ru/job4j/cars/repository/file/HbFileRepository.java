package ru.job4j.cars.repository.file;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.File;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class HbFileRepository implements FileRepository {
    private final CrudRepository cr;

    @Override
    public Optional<File> save(File file) {
        try {
            cr.run(session -> {
                session.persist(file);
                System.out.println("Persisting file: " + file.getName());
                session.flush();
            });
            return Optional.of(file);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<File> findById(int id) {
        return cr.optional("from File WHERE id = :fId",
                File.class,
                Map.of("fId", id));
    }

    @Override
    public boolean deleteById(int id) {
        int affectedRows = cr.executeDeleteOrUpdate(
                "DELETE from File WHERE id = :fId", Map.of("fId", id));
        return affectedRows > 0;
    }
}
