package ru.job4j.cars.repository.file;

import ru.job4j.cars.model.File;

import java.util.Optional;

public interface FileRepository {

    Optional<File> save(File file);

    Optional<File> findById(int id);

    boolean deleteById(int id);
}
