package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.exception.ImageNotFoundException;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import org.apache.commons.lang3.RandomStringUtils;
import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {
    @Value("${ads.image.dir.path}")
    private String imageDir;
    private final ImageRepository imageRepository;
    /**
     * @param id id сущности изображения
     * @param response ответ сервера
     */
    @Override
    public void transferImageToResponse(Integer id, HttpServletResponse response) {
        log.info("Был вызван метод для трансформации изображения для ответа{}{}", id, response);
        Image image = imageRepository.findById(id)
                .orElseThrow(()->new ImageNotFoundException("Не удалось найти изображение по id: "+ id));
        try (InputStream is = Files.newInputStream(Path.of(image.getFilePath()));
             OutputStream os = response.getOutputStream()) {
            response.setStatus(200);
            response.setContentType(image.getMediaType());
            response.setContentLength((int) image.getFileSize());
            is.transferTo(os);

        } catch (IOException e) {
            throw new RuntimeException("Failed to transfer image to response ",e);
        }
    }
    @Override
    /**
     * @param imageFile файл с изображением
     * @return файл с изображением в директории
     * @throws IOException
     */
    public Image saveImageFile(MultipartFile imageFile) throws IOException {
        log.info("Был вызван метод для сохранения файла с изображением в директорию{}", imageFile);
        Path filePath = createPathFromFile(imageFile);
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (InputStream is = imageFile.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)) {
            bis.transferTo(bos);
        }
        return saveImage(imageFile, filePath);
    }

    /**
     * @param imageFile файл с изображением
     * @return путь к файлу с изображением
     */
    private Path createPathFromFile(MultipartFile imageFile) {
        log.info("Был вызван метод генерации пути к файлу изображения");
        Path path = Path.of(String.format("%s/%s", imageDir, imageFile.getOriginalFilename()));
        if (Files.exists(path)) {
            path = Path.of(String.format("%s/%s.%s", imageDir, generateFileName(),
                    getExtension(Objects.requireNonNull(imageFile.getOriginalFilename(), ""))));
        }
        return path;
    }

    /**
     * @return сгенерированное имя файла
     */
    private String generateFileName() {
        log.info("Был вызван метод генерации имени файла");
        int length = 8;
        boolean useLetters = true;
        boolean useNumbers = true;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

    /**
     * Получение расширения файла изображения
     *
     * @param fileName полное имя файла
     * @return расширение файла
     */
    private String getExtension(String fileName) {
        log.info("Был вызван метод для получения расширения файла");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * @param imageFile файл с изображением
     * @param filePath путь
     * @return изображение с новым path
     */
    private Image saveImage(MultipartFile imageFile, Path filePath) {
        log.info("Был вызван метод для сохранения изображения в базе данных{},{}", imageFile, filePath);
        Image image = imageRepository.findByFilePath(filePath.toString()).orElse(new Image());
        image.setFilePath(filePath.toString());
        image.setFileExtension(getExtension(Objects.requireNonNull(imageFile.getOriginalFilename())));
        image.setFileSize(imageFile.getSize());
        image.setMediaType(imageFile.getContentType());
        return imageRepository.save(image);
    }

}
