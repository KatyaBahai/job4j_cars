package ru.job4j.cars.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "auto_posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    private LocalDateTime creationDate;
    private int userId;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private List<PriceHistory> priceHistoryList = new ArrayList<>();
    @ManyToMany
    @JoinTable(
            name = "participants",
            joinColumns = { @JoinColumn(name = "post_id") },
    inverseJoinColumns = { @JoinColumn(name = "user_id") })
        private List<User> participants = new ArrayList<>();
}
