package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.dto.PostFormDto;
import ru.job4j.cars.model.*;
import ru.job4j.cars.service.body.BodyService;
import ru.job4j.cars.service.brand.BrandService;
import ru.job4j.cars.service.car.CarService;
import ru.job4j.cars.service.engine.EngineService;
import ru.job4j.cars.service.file.FileService;
import ru.job4j.cars.service.owner.OwnerService;
import ru.job4j.cars.service.ownershistory.OwnersHistoryService;
import ru.job4j.cars.service.post.PostService;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@AllArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final BrandService brandService;
    private final BodyService bodyService;
    private final EngineService engineService;
    private final OwnersHistoryService ownersHistoryService;
    private final FileService fileService;

    @GetMapping("/add")
    public String getCreationPage(Model model) {
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("bodies", bodyService.findAll());
        model.addAttribute("engines", engineService.findAll());
        return "posts/create";
    }

    @PostMapping("/add")
    public String addNewPost(@SessionAttribute User wowUser,
                             @ModelAttribute PostFormDto postFormDto,
                             @RequestParam Set<MultipartFile> multipartFiles,
                             Model model) {
        Set<FileDto> dtoFiles = new HashSet<>();
        try {
            for (MultipartFile file : multipartFiles) {
                dtoFiles.add(new FileDto(file.getOriginalFilename(), file.getBytes()));
            }
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }

        Car car = Car.builder()
                .engine(engineService.findById(postFormDto.getEngineId()).get())
                .body(bodyService.findById(postFormDto.getBodyId()).get())
                .brand(brandService.findById(postFormDto.getBrandId()).get())
                .owner(postFormDto.getOwner())
                .build();

        Set<PriceHistory> priceHistorySet = new HashSet<>();
        priceHistorySet.add(PriceHistory.builder()
                .after(postFormDto.getPrice())
                .before(postFormDto.getPrice())
                .build());

        Post post = Post.builder()
                .userId(wowUser.getId())
                .creationDate(LocalDateTime.now())
                .car(car)
                .description(postFormDto.getDescription())
                .priceHistorySet(priceHistorySet)
                .build();

        Optional<Post> savedPost = postService.add(post, dtoFiles);
        if (savedPost.isEmpty()) {
            model.addAttribute("message", "There's been some problem. Please try again.");
            return "errors/404";
        }
        return "redirect:/mine";
    }

    @GetMapping("/details/{postId}")
    public String getPostById(@PathVariable int postId, Model model) {
        Optional<Post> postOptional = postService.findById(postId);
        if (postOptional.isEmpty()) {
            model.addAttribute("message", "There's no such post, sorry!");
            return "errors/404";
        }
        Post post = postOptional.get();
        model.addAttribute("post", post);
        model.addAttribute("ownersHistoryList", ownersHistoryService.findByCar(post.getCar()));
        return "posts/details";
    }

    @GetMapping("/edit/{id}")
    public String getEditPage(@PathVariable int id, Model model) {

        Optional<Post> postOptional = postService.findById(id);
        if (postOptional.isEmpty()) {
            model.addAttribute("message", "There's no such task available, sorry!");
            return "errors/404";
        }
        Post post = postOptional.get();
        model.addAttribute("price", postService.getLatestPrice(post));
        model.addAttribute("ownersHistoryList", ownersHistoryService.findByCar(post.getCar()));
        model.addAttribute("bodies", bodyService.findAll());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("engines", engineService.findAll());
        return "posts/edit";
    }

    @PostMapping("/edit")
    public String editPost(@ModelAttribute Post post,
                           @RequestParam Long price,
                           @RequestParam Set<MultipartFile> multipartFiles,
                           Model model) {
        Set<FileDto> dtoFiles = new HashSet<>();
        try {
            for (MultipartFile file : multipartFiles) {
                dtoFiles.add(new FileDto(file.getOriginalFilename(), file.getBytes()));
            }
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }

        Set<PriceHistory> priceHistorySet = new HashSet<>();
        Optional<Post> oldPostOpt = postService.findById(post.getId());
        if (oldPostOpt.isPresent()) {
            priceHistorySet = oldPostOpt.get().getPriceHistorySet();
        }
        Optional<Long> oldPrice = post.getLatestPrice();
        priceHistorySet.add(PriceHistory.builder()
                .before(oldPrice.orElse(price))
                .after(price)
                .build());
        post.setPriceHistorySet(priceHistorySet);

        fileService.clearOldFiles(post);
        Optional<Post> editedPost = postService.edit(post, dtoFiles);
        if (editedPost.isEmpty()) {
            model.addAttribute("message", "There's no post to edit with this identifier.");
            return "/errors/404";
        }
        return "redirect:/posts/mine";
    }

    @PostMapping("/status")
    public String changeStatus(@RequestParam int id,
                               @SessionAttribute User wowUser,
                               Model model) {
        boolean isChanged;
        try {
            isChanged = postService.changeSoldStatus(id, wowUser);
        } catch (RuntimeException e) {
            model.addAttribute("message", e.getMessage());
            return "/errors/404";
        }
        if (!isChanged) {
            model.addAttribute("message", "Failed to change the status of the post.");
            return "/errors/404";
        }
        return "redirect:/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, Model model) {
        var isDeleted = postService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", String.format("The post with identifier %s is not found", id));
            return "/errors/404";
        }
        return "redirect:/index";
    }

    @GetMapping("/photo-posts")
    public String getAllWithPhotos(Model model) {
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("posts", postService.findAllWithPhotos());
        return "posts/list";
    }

    @GetMapping("/brand-posts")
    public String getAllByBrand(Model model, @RequestParam int brandId) {
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("posts", postService.findAllByCarBrand(brandId));
        return "posts/list";
    }

    @GetMapping("/mine")
    public String getMyPosts(Model model, @SessionAttribute User wowUser) {
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("posts", postService.findMyPosts(wowUser.getId()));
        return "posts/mine";
    }

    @GetMapping("/today-posts")
    public String getAllTodayPosts(Model model) {
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("posts", postService.findAllWithTodayCreationDate());
        return "posts/list";
    }

    @GetMapping("/advanced")
    public String getAdvancedSearch(Model model) {
        List<Integer> priceSteps = new ArrayList<>();
        for (int i = 10000; i <= 500000; i += 50000) {
            priceSteps.add(i);
        }
        model.addAttribute("priceSteps", priceSteps);
        model.addAttribute("brands", brandService.findAll());
        return "posts/advanced";
    }

    @GetMapping("/list")
    public String getFilteredPosts(@RequestParam(required = false) Integer brandId,
                                   @RequestParam(required = false) Integer minYear,
                                   @RequestParam(required = false) Integer maxPrice,
                                   @RequestParam(required = false) Boolean hasPhoto,
                                   Model model) {
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("posts", postService.filterPosts(brandId, minYear, maxPrice, hasPhoto));
        model.addAttribute("search-info",
                String.format("Search results for: %s brand, %s minYear, %s maxPrice, hasPhoto: %s.",
                        brandId, minYear, maxPrice, hasPhoto));
        return "posts/list";
    }
}
