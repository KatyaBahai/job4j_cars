package ru.job4j.cars.service.owner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.repository.owner.OwnerRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SimpleOwnerService implements OwnerService {
    private final OwnerRepository ownerRepository;

    @Override
    public Optional<Owner> save(Owner owner) {
        return ownerRepository.add(owner);
    }

    @Override
    public boolean deleteById(int id) {
        return ownerRepository.deleteById(id);
    }

    @Override
    public Optional<Owner> update(Owner owner) {
        return ownerRepository.edit(owner);
    }

    @Override
    public Optional<Owner> findById(int id) {
        return ownerRepository.findById(id);
    }

    @Override
    public Collection<Owner> findAllByCarId(int carId) {
        return ownerRepository.findAllByCarId(carId);
    }
}
