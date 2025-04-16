package ru.job4j.cars.repository.user;

import ru.job4j.cars.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> save(User user);
}
