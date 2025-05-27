package ru.job4j.cars.service.file;

import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.model.File;

import java.util.Optional;

public interface FileSerice {

    Optional<File> save(FileDto fileDto);

    Optional<FileDto> getFileById(int id);

    void deleteById(int id);
}
