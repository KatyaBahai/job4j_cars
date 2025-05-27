package ru.job4j.cars.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@Entity
@Table(name = "owners")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "carsHistory")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<OwnersHistory> carsHistory = new HashSet<>();
}
