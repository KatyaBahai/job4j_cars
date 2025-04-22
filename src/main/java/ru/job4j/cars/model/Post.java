package ru.job4j.cars.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "auto_posts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    private String description;
    private LocalDateTime creationDate;
    private int userId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private Set<PriceHistory> priceHistoryList = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "participants",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> participants = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "post_files",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "file_id")})
    private Set<File> files = new HashSet<>();
}
