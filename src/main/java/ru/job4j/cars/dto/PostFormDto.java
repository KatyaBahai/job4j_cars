package ru.job4j.cars.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.job4j.cars.model.*;

import java.time.LocalDate;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endAt;
}
