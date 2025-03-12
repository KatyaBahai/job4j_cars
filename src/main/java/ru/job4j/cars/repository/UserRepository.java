package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class UserRepository {
    private final SessionFactory sf;

    public User create(User user) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sf.openSession();
            tx = session.beginTransaction();
            session.save(user);
            tx.commit();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return user;
    }

    public void update(User user) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sf.openSession();
            tx = session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public boolean delete(int userId) {
        boolean deleted = false;
        Session session = null;
        Transaction tx = null;
        try {
            session = sf.openSession();
            tx = session.beginTransaction();
            session.createQuery(
                            "DELETE User WHERE id = :userId")
                    .setParameter("userId", userId)
                    .executeUpdate();
            tx.commit();
            deleted = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return deleted;
    }

    public List<User> findAllOrderById() {
        try (Session session = sf.openSession()) {
            List<User> orderedUsers = session.createQuery("from User u ORDER BY u.id ASC", User.class).list();
            return orderedUsers;
        }
    }

    public Optional<User> findById(int userId) {
        try (Session session = sf.openSession()) {
            Query<User> query = session.createQuery("from User u WHERE u.id = :userId", User.class)
                    .setParameter("userId", userId);
            return query.uniqueResultOptional();
        }
    }

    public List<User> findByLikeLogin(String key) {
        try (Session session = sf.openSession()) {
            List<User> likeUsers = session.createQuery("from User u WHERE u.login LIKE :key", User.class)
                    .setParameter("key",  "%" + key + "%")
                    .list();
            return likeUsers;
        }
    }

    public Optional<User> findByLogin(String login) {
        try (Session session = sf.openSession()) {
            Query<User> query = session.createQuery("from User u WHERE u.login = :login", User.class)
                    .setParameter("login", login);
            return query.uniqueResultOptional();
        }
    }
}