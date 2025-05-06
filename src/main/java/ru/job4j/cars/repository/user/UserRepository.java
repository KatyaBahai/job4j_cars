package ru.job4j.cars.repository.user;

import ru.job4j.cars.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void update(User user);

    List<User> findAllOrderById();

    Optional<User> findById(int userId);

    List<User> findByLikeLogin(String key);

    Optional<User> findByLogin(String login);

    Optional<User> findByLoginAndPassword(String login, String password);

    Optional<User> save(User user);

    boolean deleteById(int id);

    Collection<User> findAll();
}
