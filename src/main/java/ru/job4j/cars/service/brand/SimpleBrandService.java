package ru.job4j.cars.service.brand;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Brand;
import ru.job4j.cars.repository.brand.BrandRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SimpleBrandService implements BrandService {
    private final BrandRepository brandRepository;

    @Override
    public Collection<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public Optional<Brand> findById(int id) {
        return brandRepository.findById(id);
    }
}
