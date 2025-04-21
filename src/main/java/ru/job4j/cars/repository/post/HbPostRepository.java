package ru.job4j.cars.repository.post;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class HbPostRepository implements PostRepository {
    private final CrudRepository cr;

    @Override
    public Optional<Post> add(Post post) {
        try {
            cr.run(session -> session.persist(post));
            return Optional.of(post);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Collection<Post> findAll() {
        return cr.query("from Post", Post.class);
    }

    @Override
    public Collection<Post> findAllWithPhotos() {
        return cr.query("FROM Post p JOIN FETCH p.files f", Post.class);
    }

    @Override
    public Collection<Post> findAllWithTodayCreationDate() {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime startOfTomorrow = startOfToday.plusDays(1);
        return cr.query(
                "from Post p WHERE p.creationDate >= :start AND p.creationDate < :end",
                Post.class,
                Map.of("start", startOfToday, "end", startOfTomorrow)
        );
    }

    @Override
    public Collection<Post> findAllByCarBrand(int brandId) {
        return cr.query("from Post p WHERE p.car.brand.id = :brandId",
                Post.class,
                Map.of("brandId", brandId));
    }

    @Override
    public Optional<Post> findById(int id) {
        return cr.optional(
                "SELECT DISTINCT p from Post p JOIN FETCH p.car LEFT JOIN FETCH p.files WHERE p.id = :fId",
                Post.class,
                Map.of("fId", id)
        );
    }

    @Override
    public Optional<Post> edit(Post post) {
        try {
            cr.run(session -> session.merge(post));

            return Optional.of(post);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteById(int id) {
        int affectedRows = cr.executeDeleteOrUpdate("DELETE FROM Post WHERE id = :id", Map.of("id", id));
        return affectedRows > 0;
    }
}
