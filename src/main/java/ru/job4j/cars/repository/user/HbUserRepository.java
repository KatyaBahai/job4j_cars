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
            crudRepository.run(session -> session.save(user));
            return Optional.of(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    public void update(User user) {
        crudRepository.run(session -> session.merge(user));
    }

    public List<User> findAllOrderById() {
        return crudRepository.query("from User order by id asc", User.class);
    }

    public Optional<User> findById(int userId) {
        return crudRepository.optional(
                "from User where id = :fId", User.class,
                Map.of("fId", userId)
        );
    }

    public List<User> findByLikeLogin(String key) {
        return crudRepository.query(
                "from User where login like :fKey", User.class,
                Map.of("fKey", "%" + key + "%")
        );
    }

    public Optional<User> findByLogin(String login) {
        return crudRepository.optional(
                "from User where login = :fLogin", User.class,
                Map.of("fLogin", login)
        );
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return crudRepository.optional("from User WHERE email = :email AND password = :password",
                User.class,
                Map.of("email", email, "password", password));
    }

    public boolean deleteById(int id) {
        int affectedRows = crudRepository.executeDeleteOrUpdate("DELETE User WHERE id = :id", Map.of("id", id));
        return affectedRows > 0;
    }

    public Collection<User> findAll() {
        return crudRepository.query("from User", User.class);
    }
}