package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * <b>Реализация сервиса для работы с изображениями. </b> <p>
 *
 * Этот класс предоставляет методы для загрузки, удаления и получения изображений.
 *
 * <p>Основные функции:</p>
 * <ul>
 *     <li>Загрузка изображения на сервер.</li>
 *     <li>Удаление изображения из базы данных.</li>
 *     <li>Получение изображения по его идентификатору.</li>
 * </ul>
 *
 * @author Mishator
 */
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository repository;
    public static String uploadDirectory = System.getProperty("user.dir") + "/images";

    /**
     * <b>Загружает изображение на сервер и сохраняет информацию о нем в базе данных. </b> <p>
     *
     * @param id идентификатор, связанный с изображением
     * @param imageFile файл изображения, который нужно загрузить
     * @return объект {@link Image}, содержащий информацию о загруженном изображении
     * @throws IOException если произошла ошибка при чтении или записи файла
     */
    @Override
    public Image uploadImage(long id, MultipartFile imageFile) throws IOException {

        Path filePath = Path.of(uploadDirectory, "listing_" + id + "." + getExtensions(imageFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (
                InputStream is = imageFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);

        ) {
            bis.transferTo(bos);
        }
        Image image = new Image();
        image.setFilePath(filePath.toString());
        image.setFileSize(imageFile.getSize());
        image.setMediaType(imageFile.getContentType());
        image.setData(imageFile.getBytes());
        repository.save(image);
        return image;
    }

    /**
     * <b>Удаляет изображение из базы данных. </b> <p>
     *
     * @param image объект {@link Image}, который нужно удалить
     */
    @Override
    public void removeImage(Image image) {
        repository.delete(image);

    }

    /**
     * <b>Получает изображение по его идентификатору. </b> <p>
     *
     * @param id идентификатор изображения
     * @return объект {@link Image}, если изображение найдено, иначе новый объект {@link Image}
     */
    @Override
    public Image getImage(Long id) {
        return repository.findById(id).orElse(new Image());
    }

    /**
     * <b>Получает расширение файла из имени файла. </b> <p>
     *
     * @param fileName имя файла, из которого нужно извлечь расширение
     * @return строка, представляющая расширение файла
     */
    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}
