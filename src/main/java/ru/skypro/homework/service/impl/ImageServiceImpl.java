package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
@Service
public class ImageServiceImpl implements ImageService {
    @Value("${images.root}")
    private String imagesRoot;


    @Override
    public String saveImage(MultipartFile file, int id, String type) throws IOException {
        Path pathDir = Paths.get(imagesRoot, type, String.valueOf(id));
        Files.createDirectories(pathDir);

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = pathDir.resolve(fileName);
        Files.write(filePath, file.getBytes());
        return "/" + type + "/" + id + "/" + fileName;
    }

    @Override
    public void deleteImage(String filePath) throws IOException {
        if (filePath != null && !filePath.isBlank()) {
            Path path = Paths.get(imagesRoot + filePath);
            Files.deleteIfExists(path);
        }
    }
}
