package ru.job4j.cars.service.post;

import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.model.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostService {
    Optional<Post> add(Post post, List<FileDto> fileDtos);

    Collection<Post> findAll();

    Collection<Post> findAllWithPhotos();

    Collection<Post> findAllWithTodayCreationDate();

    Collection<Post> findAllByCarBrand(int brandId);

    Optional<Post> findById(int id);

    Optional<Post> edit(Post post, List<FileDto> fileDtos);

    boolean deleteById(int id);

    boolean changeSoldStatus(int postId, User user);

    Optional<Long> getLatestPrice(Post post);

    Collection<Post> filterPosts(Integer brandId,
                                 Integer minYear,
                                 Boolean hasPhoto, Integer bodyId);

    Collection<Post> findMyPosts(int userId);
}

