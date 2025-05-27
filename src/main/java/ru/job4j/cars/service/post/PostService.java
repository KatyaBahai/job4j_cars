package ru.job4j.cars.service.post;

import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;

import java.util.Collection;
import java.util.Optional;

public interface PostService {
    Optional<Post> add(Post post);

    Collection<Post> findAll();

    Collection<Post> findAllWithPhotos();

    Collection<Post> findAllWithTodayCreationDate();

    Collection<Post> findAllByCarBrand(int brandId);

    Optional<Post> findById(int id);

    Optional<Post> edit(Post post);

    boolean deleteById(int id);

    boolean changeSoldStatus(int postId, User user);
}

