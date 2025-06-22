package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ImageDto;
import ru.skypro.homework.model.Image;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface ImageService {

    List<Image> getAllImages();

    Image createImage(Image image);

    Image updateImage(Integer id, Image imageDetails);

    void deleteImage(Integer id);

    Image saveImage(Integer id, MultipartFile file) throws IOException;

    void saveToDatabase(ImageDto imageDTO, Path imagePath, MultipartFile file);

    Optional<Image> findImageById(Integer id);
}
