package ru.job4j.cars.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Builder
@Entity
@Table(name = "owners_history")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OwnersHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "start_at", nullable = false)
    private LocalDate startAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "end_at")
    private LocalDate endAt;

    public void connectOwnerAndCar(Car car, Owner owner) {
        this.owner.getCarsHistory().add(this);
        this.car.getOwnersHistory().add(this);
    }

}
