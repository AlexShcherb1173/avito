package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${app.images.dir}")
    private String imagesDir;

    @Override
    public String saveImage(MultipartFile file) throws IOException {

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path path = Path.of(imagesDir, fileName);

        Files.createDirectories(path.getParent());

        Files.write(path, file.getBytes());

        return fileName;
    }

    @Override
    public byte[] getImage(String fileName) throws IOException {

        Path path = Path.of(imagesDir, fileName);

        return Files.readAllBytes(path);
    }
}