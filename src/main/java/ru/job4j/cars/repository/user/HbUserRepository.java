package ru.job4j.cars.repository.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class HbUserRepository implements UserRepository {
    private final CrudRepository crudRepository;

    @Override
    public Optional<User> save(User user) {
        try {
            crudRepository.run(session -> {
                session.persist(user);
                session.flush();
            });
            return Optional.of(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public void update(User user) {
        crudRepository.run(session -> session.merge(user));
    }

    @Override
    public List<User> findAllOrderById() {
        return crudRepository.query("from User order by id asc", User.class);
    }

    @Override
    public Optional<User> findById(int userId) {
        return crudRepository.optional(
                "from User where id = :fId", User.class,
                Map.of("fId", userId)
        );
    }

    @Override
    public List<User> findByLikeLogin(String key) {
        return crudRepository.query(
                "from User where login like :fKey", User.class,
                Map.of("fKey", "%" + key + "%")
        );
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return crudRepository.optional(
                "from User where login = :fLogin", User.class,
                Map.of("fLogin", login)
        );
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return crudRepository.optional("from User WHERE login = :login AND password = :password",
                User.class,
                Map.of("login", login, "password", password));
    }

    @Override
    public boolean deleteById(int id) {
        int affectedRows = crudRepository.executeDeleteOrUpdate("DELETE User WHERE id = :id", Map.of("id", id));
        return affectedRows > 0;
    }

    @Override
    public Collection<User> findAll() {
        return crudRepository.query("from User", User.class);
    }
}