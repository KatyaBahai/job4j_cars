package ru.job4j.cars.repository.user;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.*;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class HbUserRepositoryTest {
    private static HbUserRepository userRepository;
    private static StandardServiceRegistry registry;
    private static SessionFactory sf;

    @BeforeAll
    static void setUpUserRepository() throws Exception {
        registry = new StandardServiceRegistryBuilder().configure().build();
        sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        CrudRepository crudRepository = new CrudRepository(sf);
        userRepository = new HbUserRepository(crudRepository);
    }

    @AfterEach
    void clearData() {
        userRepository.findAll().forEach(user -> userRepository.deleteById(user.getId()));
    }

    @AfterAll
    static void destroy() {
        if (sf != null) {
            sf.close();
        }
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @Test
    void whenSaveThenSaved() {
        User user = User.builder()
                .login("admin")
                .password("admin")
                .build();
        userRepository.save(user);
        User resultUser = userRepository.findByLogin(user.getLogin()).get();
        assertThat(user)
                .usingRecursiveComparison()
                .isEqualTo(resultUser);
    }

    @Disabled
    @Test
    void whenUpdateThenUpdated() {
        User user = User.builder()
                .login("admin")
                .password("admin")
                .build();
        userRepository.save(user);
        user.setPassword("password");
        userRepository.update(user);
        User resultUser = userRepository.findById(user.getId()).get();
        assertThat(resultUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(resultUser.getId()).isEqualTo(user.getId());
    }

    @Test
    void whenFindAllOrderedByIdThenFoundAllInOrder() {
        User user1 = User.builder()
                .login("admin")
                .password("admin")
                .build();
        User user2 = User.builder()
                .login("user1")
                .password("password")
                .build();
        User user3 = User.builder()
                .login("user2")
                .password("password")
                .build();
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        Collection<User> resultUsers = userRepository.findAllOrderById();
        assertThat(resultUsers).containsExactly(user1, user2, user3);
    }

    @Test
    void whenFindByIdThenFound() {
        User user = User.builder()
                .login("admin")
                .password("admin")
                .build();
        userRepository.save(user);
        User resultUser = userRepository.findById(user.getId()).get();
        assertThat(resultUser.getLogin()).isEqualTo(user.getLogin());
    }

    @Test
    void whenFindByIdNonExistingUserThenNothingFound() {
        User user = User.builder()
                .login("admin")
                .password("admin")
                .build();
        userRepository.save(user);
        Optional<User> optUserResult = userRepository.findById(-1);
        assertThat(optUserResult).isEmpty();
    }

    @Test
    void whenFindByLikeLoginThenFoundAll() {
        User user1 = User.builder()
                .login("admin")
                .password("admin")
                .build();
        User user2 = User.builder()
                .login("user1")
                .password("password")
                .build();
        User user3 = User.builder()
                .login("user2")
                .password("password")
                .build();
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        Collection<User> resultUsers = userRepository.findByLikeLogin("e");
        assertThat(resultUsers).containsExactlyInAnyOrder(user2, user3);
    }

    @Test
    void whenFindByLoginThenFound() {
        User user = User.builder()
                .login("admin")
                .password("admin")
                .build();
        userRepository.save(user);
        User resultUser = userRepository.findByLogin("admin").get();
        assertThat(user).usingRecursiveComparison().isEqualTo(resultUser);
    }

    @Test
    void whenDeleteByIdThenDeleted() {
        User user = User.builder()
                .login("admin")
                .password("admin")
                .build();
        userRepository.save(user);
        int id = userRepository.findByLogin(user.getLogin()).get().getId();
        userRepository.deleteById(id);
        assertThat(userRepository.findAll()).isEmpty();
    }

    @Test
    void whenFindAllThenAllFound() {
        User user1 = User.builder()
                .login("admin")
                .password("admin")
                .build();
        User user2 = User.builder()
                .login("user1")
                .password("password")
                .build();
        User user3 = User.builder()
                .login("user2")
                .password("password")
                .build();
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        Collection<User> users = userRepository.findAll();
        users.forEach(System.out :: println);
        assertThat(users).containsExactlyInAnyOrder(user1, user2, user3);
    }
}