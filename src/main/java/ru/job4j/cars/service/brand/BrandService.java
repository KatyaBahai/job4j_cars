package ru.job4j.cars.service.brand;

import ru.job4j.cars.model.Brand;

import java.util.Collection;

public interface BrandService {
    Collection<Brand> findAll();
}
