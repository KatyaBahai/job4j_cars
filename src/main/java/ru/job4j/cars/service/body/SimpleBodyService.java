package ru.job4j.cars.service.body;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Body;
import ru.job4j.cars.repository.body.BodyRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SimpleBodyService implements BodyService {
    private final BodyRepository bodyRepository;

    @Override
    public Collection<Body> findAll() {
        return bodyRepository.findAll();
    }

    @Override
    public Optional<Body> findById(int id) {
        return bodyRepository.findById(id);
    }
}
