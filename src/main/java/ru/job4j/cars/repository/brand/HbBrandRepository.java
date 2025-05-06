package ru.job4j.cars.repository.brand;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Brand;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class HbBrandRepository implements BrandRepository {
    private final CrudRepository cr;

    @Override
    public Collection<Brand> findAll() {
        return cr.query("from Brand b order by b.id", Brand.class);
    }

    @Override
    public Optional<Brand> findById(int id) {
        return cr.optional("from Brand b WHERE b.id = :fId", Brand.class, Map.of("fId", id));
    }

    @Override
    public Optional<Brand> add(Brand brand) {
        try {
            cr.run(session -> {
                        session.persist(brand);
                        session.flush();
                    }
            );
            return Optional.of(brand);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM Brand WHERE id = :id";
        int affectedRows = cr.executeDeleteOrUpdate(sql, Map.of("id", id));
        return affectedRows > 0;
    }
}
