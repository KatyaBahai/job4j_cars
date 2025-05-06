package ru.job4j.cars.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@Entity
@Table(name = "owners")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    private String name;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "owners_history", joinColumns = {
            @JoinColumn(name = "owners_id", nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "car_id", nullable = false, updatable = false)})
    private Set<Car> cars = new HashSet<>();

}
