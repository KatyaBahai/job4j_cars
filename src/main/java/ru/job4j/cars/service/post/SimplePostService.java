package ru.job4j.cars.service.post;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.post.PostRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimplePostService implements  PostService {
    private final PostRepository postRepository;

    @Override
    public Optional<Post> add(Post post) {
        return postRepository.add(post);
    }

    @Override
    public Collection<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Collection<Post> findAllWithPhotos() {
        return postRepository.findAllWithPhotos();
    }

    @Override
    public Collection<Post> findAllWithTodayCreationDate() {
        return postRepository.findAllWithTodayCreationDate();
    }

    @Override
    public Collection<Post> findAllByCarBrand(int brandId) {
        return postRepository.findAllByCarBrand(brandId);
    }

    @Override
    public Optional<Post> findById(int id) {
        return postRepository.findById(id);
    }

    @Override
    public Optional<Post> edit(Post post) {
        return postRepository.edit(post);
    }

    @Override
    public boolean deleteById(int id) {
        return postRepository.deleteById(id);
    }

    @Override
    public boolean changeSoldStatus(int postId, User user) {
       Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new RuntimeException("No Post was found with id " + postId);
        }
        if (post.get().getUserId() != user.getId()) {
            throw new RuntimeException("Only the author can change the post status.");
        }
        return postRepository.changeSoldStatus(postId);
    }
}
