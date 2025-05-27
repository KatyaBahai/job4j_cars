package ru.job4j.cars.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

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

    @Column(name = "start_at", nullable = false)
    private Instant startAt;

    @Column(name = "end_at")
    private Instant endAt;

    public void setCar(Car car) {
        this.car = car;
        this.owner.getCarsHistory().add(this);
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
        this.car.getOwnersHistory().add(this);
    }
}
