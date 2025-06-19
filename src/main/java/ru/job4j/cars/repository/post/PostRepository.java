package ru.job4j.cars.repository.post;


import ru.job4j.cars.model.Post;

import java.util.Collection;
import java.util.Optional;

public interface PostRepository {
    Optional<Post> add(Post post);

    Collection<Post> findAll();

    Collection<Post> findAllWithPhotos();

    Collection<Post> findAllWithTodayCreationDate();

    Collection<Post> findAllByCarBrand(int brandId);

    Optional<Post> findById(int id);

    Collection<Post> findMyPosts(int userId);

    Collection<Post> filterPosts(Integer brandId, Integer minYear, Boolean hasPhoto, Integer bodyId);

    Optional<Post> edit(Post post);

    boolean deleteById(int id);

    boolean changeSoldStatus(int postId);
}
