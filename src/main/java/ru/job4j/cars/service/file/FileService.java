package ru.job4j.cars.service.file;

import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.model.File;
import ru.job4j.cars.model.Post;

import java.util.Optional;

public interface FileService {

    Optional<File> save(FileDto fileDto);

    Optional<FileDto> getFileById(int id);

    void deleteById(int id);

    void clearOldFiles(Post post);
}
