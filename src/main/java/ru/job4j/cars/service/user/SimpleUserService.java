package ru.job4j.cars.service.user;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
    private final SessionFactory sf;

    @Override
    public void update(User user) {
        userRepository.update(user);
    }

    @Override
    public List<User> findAllOrderById() {
        return userRepository.findAllOrderById();
    }

    @Override
    public Optional<User> findById(int userId) {
        Session session = sf.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Optional<User> userOpt = userRepository.findById(userId);
            transaction.commit();
            return userOpt;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<User> findByLikeLogin(String key) {
        return userRepository.findByLikeLogin(key);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public boolean deleteById(int id) {
        return userRepository.deleteById(id);
    }

    @Override
    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        Session session = sf.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Optional<User> userOpt = userRepository.findByLoginAndPassword(login, password);
            transaction.commit();
            return userOpt;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
