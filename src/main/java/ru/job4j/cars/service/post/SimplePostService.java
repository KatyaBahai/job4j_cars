package ru.job4j.cars.service.post;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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

@Transactional
@Service
@RequiredArgsConstructor
public class SimplePostService implements PostService {
    private final PostRepository postRepository;
    private final FileService fileService;
    private final SessionFactory sf;

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
        Session session = sf.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Collection<Post> posts = postRepository.findAll();
            Collection<Post> postsWithCollections = getPostsWithFilesAndPriceHistorySet(posts);
            transaction.commit();
            return postsWithCollections;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public Collection<Post> findAllWithPhotos() {
        Collection<Post> posts = postRepository.findAllWithPhotos();
        return getPostsWithFilesAndPriceHistorySet(posts);
    }

    @Override
    public Collection<Post> findAllWithTodayCreationDate() {

        Collection<Post> posts = postRepository.findAllWithTodayCreationDate();
        return getPostsWithFilesAndPriceHistorySet(posts);
    }

    @Override
    public Collection<Post> findAllByCarBrand(int brandId) {
        Collection<Post> posts = postRepository.findAllByCarBrand(brandId);
        return getPostsWithFilesAndPriceHistorySet(posts);
    }

    @Override
    public Optional<Post> findById(int id) {
       Optional<Post> postOpt = postRepository.findById(id);
       if (postOpt.isPresent()) {
           Post post = postOpt.get();
           post.getFiles().size();
           post.getPriceHistorySet().size();
           return Optional.of(post);
       }
        return postOpt;
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

        Collection<Post> posts;
        if (brandId == null && minYear == null && bodyId == null && hasPhoto == null) {
            posts = postRepository.findAll();

        } else {
            posts = postRepository.filterPosts(brandId, minYear, hasPhoto, bodyId);
        }
        return getPostsWithFilesAndPriceHistorySet(posts);
    }

    public Collection<Post> findMyPosts(int userId) {
            Collection<Post> posts = postRepository.findMyPosts(userId);
            return getPostsWithFilesAndPriceHistorySet(posts);
    }

    private Collection<Post> getPostsWithFilesAndPriceHistorySet(Collection<Post> posts) {
        for (Post post : posts) {
            post.getFiles().size();
            post.getPriceHistorySet().size();
        }
        return posts;
    }
}
