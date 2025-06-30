package ru.job4j.cars.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.user.UserRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SimpleUserService implements UserService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void update(User user) {
        userRepository.update(user);
    }

    @Transactional
    @Override
    public List<User> findAllOrderById() {
        return userRepository.findAllOrderById();
    }

    @Transactional
    @Override
    public Optional<User> findById(int userId) {
        return userRepository.findById(userId);
    }

    @Transactional
    @Override
    public List<User> findByLikeLogin(String key) {
        return userRepository.findByLikeLogin(key);
    }

    @Transactional
    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Transactional
    @Override
    public boolean deleteById(int id) {
        return userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public Optional<User> save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password);
    }
}
