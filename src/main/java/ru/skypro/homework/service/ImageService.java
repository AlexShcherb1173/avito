package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Image;

import java.io.IOException;
import java.nio.file.Path;

public interface ImageService {
    Image addImage(int id, MultipartFile file) throws IOException;
}
