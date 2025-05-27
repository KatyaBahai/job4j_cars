package ru.job4j.cars.service.engine;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.engine.EngineRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class SimpleEngineService implements EngineService {
    private final EngineRepository engineRepository;

    @Override
    public Collection<Engine> findAll() {
        return engineRepository.findAll();
    }
}
