package ru.job4j.cars.service.ownershistory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.model.OwnersHistory;
import ru.job4j.cars.repository.car.CarRepository;
import ru.job4j.cars.repository.owner.OwnerRepository;
import ru.job4j.cars.repository.ownershistory.OwnersHistoryRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimpleOwnersHistoryService implements OwnersHistoryService {
    private final OwnersHistoryRepository ownersHistoryRepository;
    private final CarRepository carRepository;
    private final OwnerRepository ownerRepository;

    @Override
    public Optional<OwnersHistory> add(OwnersHistory newOwnerHistory) {
        int carId = newOwnerHistory.getCar().getId();
        int ownerId = newOwnerHistory.getOwner().getId();

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("Car not found"));
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Owner not found"));

        boolean hasCurrentOwner = ownersHistoryRepository
                .findByCar(car)
                .stream()
                .anyMatch(ownersHistory ->  ownersHistory.getEndAt() == null);

        if (hasCurrentOwner && newOwnerHistory.getEndAt() == null) {
            throw new IllegalStateException("This car already has a current owner.");
        }

        newOwnerHistory.setCar(car);
        newOwnerHistory.setOwner(owner);
        return ownersHistoryRepository.add(newOwnerHistory);
    }

    @Override
    public Collection<OwnersHistory> findByCar(Car car) {
        return ownersHistoryRepository.findByCar(car);
    }

    @Override
    public Collection<OwnersHistory> findByOwner(Owner owner) {
        return ownersHistoryRepository.findByOwner(owner);
    }

    @Override
    public boolean deleteById(int id) {
        return ownersHistoryRepository.deleteById(id);
    }

    @Override
    public Optional<OwnersHistory> update(OwnersHistory ownersHistory) {
        return ownersHistoryRepository.update(ownersHistory);
    }

    @Override
    public Optional<OwnersHistory> findCurrentOwner() {
        return ownersHistoryRepository.findCurrentOwner();
    }
}
