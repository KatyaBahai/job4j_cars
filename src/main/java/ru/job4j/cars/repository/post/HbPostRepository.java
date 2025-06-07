package ru.job4j.cars.repository.post;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public boolean changeSoldStatus(int postId) {
        return cr.executeDeleteOrUpdate(
                "UPDATE Post p SET p.sold = CASE WHEN p.sold = true THEN false ELSE true END WHERE p.id = :postId",
                Map.of("postId", postId)) > 0;
    }

    @Override
    public Collection<Post>  findMyPosts(int userId) {
        return cr.query(
                "SELECT DISTINCT p FROM Post p "
                        + "JOIN p.car c "
                        + "JOIN c.brand b "
                        + "LEFT JOIN FETCH p.files "
                        + "WHERE p.userId = :userId",
                Post.class,
                Map.of("userId", userId)
        );
    }

    @Override
    public Collection<Post> filterPosts(Integer brandId, Integer minYear, Integer maxPrice, Boolean hasPhoto) {
        Map<String, Object> argsMap = new HashMap<>();

        StringBuilder hql = new StringBuilder("SELECT DISTINCT p FROM Post p "
                + "JOIN p.car c "
                + "JOIN c.brand b "
                + "LEFT JOIN FETCH p.files ");

        if (brandId != null) {
            hql.append(" WHERE p.brand.id = :brandId");
            argsMap.put("brandId", brandId);
        }
        if (minYear != null) {
            hql.append(" AND p.year >= :minYear");
            argsMap.put("minYear", minYear);
        }
        if (maxPrice != null) {
            hql.append(" AND p.price <= :maxPrice");
            argsMap.put("maxPrice", maxPrice);
        }
        if (Boolean.TRUE.equals(hasPhoto)) {
            hql.append(" AND SIZE(p.files) > 0");
        }
        return cr.query(
                hql.toString(),
                Post.class,
                argsMap);
    }
}
