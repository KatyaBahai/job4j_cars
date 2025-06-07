package ru.job4j.cars.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.job4j.cars.model.*;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostFormDto {
    private int price;
    // Car fields
    private int brandId;
    private int bodyId;
    private int engineId;
    private Owner owner;
    private int year;
    // Post fields
    private String description;
    // OwnersHistory fields
    private Instant startAt;
    private Instant endAt;
}
