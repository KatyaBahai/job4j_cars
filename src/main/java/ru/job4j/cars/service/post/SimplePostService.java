package ru.job4j.cars.service.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.model.File;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.post.PostRepository;
import ru.job4j.cars.service.file.FileService;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SimplePostService implements PostService {
    private final PostRepository postRepository;
    private final FileService fileService;

    @Override
    public Optional<Post> add(Post post, List<FileDto> fileDtos) {
        saveNewFiles(post, fileDtos);
        return postRepository.add(post);
    }

    private void saveNewFiles(Post post, List<FileDto> fileDtos) {
        Set<File> files = post.getFiles();
        for (FileDto fileDto : fileDtos) {
            System.out.println("Saving file: " + fileDto.getName());
            var file = fileService.save(fileDto);
            file.ifPresent(files::add);
        }
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
    public Optional<Post> edit(Post post, List<FileDto> fileDtos) {
        saveNewFiles(post, fileDtos);
        return postRepository.edit(post);
    }

    @Override
    public boolean deleteById(int id) {
        return postRepository.deleteById(id);
    }

    @Override
    public boolean changeSoldStatus(int postId, User user) throws RuntimeException {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new RuntimeException("No Post was found with id " + postId);
        }
        if (post.get().getUserId() != user.getId()) {
            throw new RuntimeException("Only the author can change the post status.");
        }
        return postRepository.changeSoldStatus(postId);
    }

    @Override
    public Optional<Long> getLatestPrice(Post post) {
        if (post.getPriceHistorySet().isEmpty()) {
            return Optional.of(0L);
        }
        return post.getPriceHistorySet().stream()
                .max(Comparator.comparing(PriceHistory::getCreated))
                .map(PriceHistory::getAfter);
    }

    @Override
    public Collection<Post> filterPosts(Integer brandId,
                                        Integer minYear,
                                        Boolean hasPhoto,
                                        Integer bodyId) {

        if (brandId == null && minYear == null && bodyId == null && hasPhoto == null) {
            return postRepository.findAll();
        } else {
            Collection<Post> posts = postRepository.filterPosts(brandId, minYear, hasPhoto, bodyId);
            posts.forEach(p -> p.getPriceHistorySet().size());
            return posts;
        }
    }

    public Collection<Post> findMyPosts(int userId) {
        return postRepository.findMyPosts(userId);
    }
}
