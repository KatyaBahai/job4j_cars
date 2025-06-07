package ru.job4j.cars.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Builder
@Entity
@Table(name = "price_history")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class PriceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private long before;
    private long after;
    private Instant created;
}
