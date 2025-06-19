package ru.job4j.cars.repository.post;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.*;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.brand.BrandRepository;
import ru.job4j.cars.repository.brand.HbBrandRepository;
import ru.job4j.cars.repository.car.CarRepository;
import ru.job4j.cars.repository.car.HbCarRepository;
import ru.job4j.cars.repository.engine.EngineRepository;
import ru.job4j.cars.repository.engine.HbEngineRepository;
import ru.job4j.cars.repository.owner.HbOwnerRepository;
import ru.job4j.cars.repository.owner.OwnerRepository;
import ru.job4j.cars.repository.user.HbUserRepository;
import ru.job4j.cars.repository.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@Disabled("Fails due to known issue with persistence. To be fixed")
class HbPostRepositoryTest {

    private static StandardServiceRegistry registry;
    private static SessionFactory sf;

    private static HbPostRepository postRepository;
    private static BrandRepository brandRepository;
    private static CarRepository carRepository;
    private static EngineRepository engineRepository;
    private static OwnerRepository ownerRepository;

    private static UserRepository userRepository;
    private static User user;

    @BeforeAll
    static void setUpPostRepository() throws Exception {
        registry = new StandardServiceRegistryBuilder().configure().build();
        sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        CrudRepository crudRepository = new CrudRepository(sf);
        postRepository = new HbPostRepository(crudRepository);
        brandRepository = new HbBrandRepository(crudRepository);
        carRepository = new HbCarRepository(crudRepository);
        engineRepository = new HbEngineRepository(crudRepository);
        userRepository = new HbUserRepository(crudRepository);
        ownerRepository = new HbOwnerRepository(crudRepository);
    }

    @BeforeEach
    void setupUser() {
        Optional<User> existing = userRepository.findByLogin("admin");
        if (existing.isEmpty()) {
            User user1 = User.builder()
                    .login("admin")
                    .password("password")
                    .build();
            userRepository.save(user1);
        }
        user = userRepository.findByLogin("admin").get();
        System.out.println(user.getId() + " 88888888888");
    }

    @AfterEach
    void clearData() {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete from Post").executeUpdate();
        session.createQuery("delete from Car").executeUpdate();
        session.createQuery("delete from Brand").executeUpdate();
        session.createQuery("delete from User").executeUpdate();
        session.createQuery("delete from File").executeUpdate();
        session.getTransaction().commit();

           }

    @AfterAll
    static void destroy() {
        userRepository.deleteById(user.getId());
        if (sf != null) {
            sf.close();
        }
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @Test
    void whenAddNewPostThenAdded() {
        Post post = Post.builder()
                .creationDate(LocalDateTime.now())
                .userId(user.getId())
                .description("-")
                .build();
        Optional<Post> savedPostOpt = postRepository.add(post);
        assertThat(savedPostOpt).isPresent();
        Post savedPost = savedPostOpt.get();

        assertThat(savedPost).usingRecursiveComparison().isEqualTo(post);
        assertThat(savedPost.getDescription()).isEqualTo(post.getDescription());

    }

    @Test
    void whenUpdateThenUpdated() {
        Post post = Post.builder()
                .creationDate(LocalDateTime.now())
                .userId(user.getId())
                .description("-")
                .build();
        postRepository.add(post);
        post.setDescription("description");

        postRepository.edit(post);
        System.out.println(post.getId());
        Post resultPost = postRepository.findById(post.getId()).get();
        assertThat(resultPost.getDescription()).isEqualTo(post.getDescription());
        assertThat(resultPost.getId()).isEqualTo(post.getId());
    }

    @Test
    void whenFindByIdThenFound() {
        Post post = Post.builder()
                .creationDate(LocalDateTime.now())
                .userId(user.getId())
                .description("-")
                .build();
        post = postRepository.add(post).get();
        Post resultPost = postRepository.findById(post.getId()).get();
        assertThat(resultPost.getDescription()).isEqualTo(post.getDescription());
    }

    @Test
    void whenFindByIdNonExistingUserThenNothingFound() {
        Post post = Post.builder()
                .creationDate(LocalDateTime.now())
                .userId(user.getId())
                .description("-")
                .build();
        postRepository.add(post);

        Optional<Post> optPostResult = postRepository.findById(-1);
        assertThat(optPostResult).isEmpty();
    }

    @Test
    void whenDeleteByIdThenDeleted() {
        Post post = Post.builder()
                .creationDate(LocalDateTime.now())
                .userId(user.getId())
                .description("-")
                .build();
        postRepository.add(post);

        postRepository.deleteById(post.getId());
        assertThat(postRepository.findAll()).isEmpty();
    }

    @Test
    void whenFindAllThenAllFound() {
        Post post1 = Post.builder()
                .creationDate(LocalDateTime.now())
                .userId(user.getId())
                .description("-")
                .build();
        Post post2 = Post.builder()
                .creationDate(LocalDateTime.now())
                .userId(user.getId())
                .description("--")
                .build();
        Post post3 = Post.builder()
                .creationDate(LocalDateTime.now())
                .userId(user.getId())
                .description("---")
                .build();

        assertThat(postRepository.add(post1)).isPresent();
        assertThat(postRepository.add(post2)).isPresent();
        assertThat(postRepository.add(post3)).isPresent();

        Collection<Post> posts = postRepository.findAll();
        assertThat(posts).containsExactlyInAnyOrder(post1, post2, post3);
    }

    @Test
    void whenFindAllWithPhotosThenAllWithPhotosFound() {
        File file = new File(0, "Screenshot_1", "Screenshot_1.png");
        Post post1 = Post.builder()
                .creationDate(LocalDateTime.now())
                .files(Set.of(file))
                .userId(user.getId())
                .description("--")
                .build();
        Post post2 = Post.builder()
                .creationDate(LocalDateTime.now())
                .userId(user.getId())
                .description("-")
                .build();
        Post post3 = Post.builder()
                .creationDate(LocalDateTime.now())
                .userId(user.getId())
                .description("---")
                .build();
        postRepository.add(post1);
        postRepository.add(post2);
        postRepository.add(post3);

        Collection<Post> resultPost = postRepository.findAll();
        System.out.println(post1.getFiles().size());
        System.out.println(resultPost.size());
        Collection<Post> resultPosts = postRepository.findAllWithPhotos();
        assertThat(resultPosts).containsExactlyInAnyOrder(post1);
    }

    @Test
    void whenFindAllWithTodayCreationDateThenFound() {
        Post post1 = Post.builder()
                .userId(user.getId())
                .creationDate(LocalDateTime.now().minusDays(7))
                .description("--")
                .build();
        Post post2 = Post.builder()
                .creationDate(LocalDateTime.now())
                .userId(user.getId())
                .description("-")
                .build();
        Post post3 = Post.builder()
                .creationDate(LocalDateTime.now())
                .userId(user.getId())
                .description("---")
                .build();
        postRepository.add(post1);
        postRepository.add(post2);
        postRepository.add(post3);

        Collection<Post> resultPosts = postRepository.findAllWithTodayCreationDate();
        assertThat(resultPosts).containsExactlyInAnyOrder(post2, post3);
    }

    @Test
    void whenFindAllByCarBrandThenFound() {
        Brand brand1 = new Brand(0, "Toyota");
        Brand brand2 = new Brand(0, "Ford");
        brandRepository.add(brand1);
        brandRepository.add(brand2);

        Engine engine = new Engine(0, "1234");
        engineRepository.add(engine);

        Owner owner = Owner.builder().name("owner").build();
        ownerRepository.add(owner);

        Car toyotaCar = Car.builder()
                .owner(owner)
                .name("Toyota Corolla")
                .brand(brand1)
                .engine(engine)
                .build();

        Car fordCar = Car.builder()
                .owner(owner)
                .name("Ford Focus")
                .brand(brand2)
                .engine(engine)
                .build();

        carRepository.add(toyotaCar);
        carRepository.add(fordCar);

        Post post1 = Post.builder()
                .car(toyotaCar)
                .creationDate(LocalDateTime.now())
                .userId(user.getId())
                .description("--")
                .build();
        Post post2 = Post.builder()
                .creationDate(LocalDateTime.now())
                .userId(user.getId())
                .car(fordCar)
                .description("-")
                .build();
        Post post3 = Post.builder()
                .creationDate(LocalDateTime.now())
                .userId(user.getId())
                .car(fordCar)
                .description("---")
                .build();
        postRepository.add(post1);
        postRepository.add(post2);
        postRepository.add(post3);

        Collection<Post> resultPosts = postRepository.findAllByCarBrand(toyotaCar.getBrand().getId());
        assertThat(resultPosts).containsExactlyInAnyOrder(post1);
    }
}