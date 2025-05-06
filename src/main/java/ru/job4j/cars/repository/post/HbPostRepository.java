package ru.job4j.cars.repository.post;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.CrudRepository;

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
            cr.run(session -> {
                        session.persist(post);
                        session.flush();
                        System.out.println("Post ID after save: " + post.getId());
                    }
            );
            return Optional.of(post);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Collection<Post> findAll() {
        return cr.query("SELECT DISTINCT p from Post p LEFT JOIN FETCH p.files", Post.class);
    }

    @Override
    public Collection<Post> findAllWithPhotos() {
        return cr.query("SELECT DISTINCT p FROM Post p JOIN FETCH p.files WHERE SIZE(p.files) > 0", Post.class);
    }

    @Override
    public Collection<Post> findAllWithTodayCreationDate() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        return cr.query(
                "SELECT DISTINCT p from Post p LEFT JOIN FETCH p.files WHERE p.creationDate > :yesterday",
                Post.class,
                Map.of("yesterday", yesterday)
        );
    }

    @Override
    public Collection<Post> findAllByCarBrand(int brandId) {
        return cr.query(
                "SELECT DISTINCT p FROM Post p "
                        + "JOIN p.car c "
                        + "JOIN c.brand b "
                        + "LEFT JOIN FETCH p.files "
                        + "WHERE b.id = :brandId",
                Post.class,
                Map.of("brandId", brandId)
        );
    }

    @Override
    public Optional<Post> findById(int id) {
        return cr.optional(
                "SELECT DISTINCT p from Post p LEFT JOIN FETCH p.car LEFT JOIN FETCH p.files WHERE p.id = :fId",
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
