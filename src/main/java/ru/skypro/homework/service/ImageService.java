package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Image;

import java.io.IOException;

public interface ImageService {
    /**
     * Загружает изображение и сохраняет его в файловой системе.
     *
     * @param file Файл изображения.
     * @return Объект Image с информацией о сохраненном изображении.
     * @throws IOException Если произошла ошибка при сохранении файла.
     */
    Image uploadImage(MultipartFile file) throws IOException;

    /**
     * Получает изображение по его ID.
     *
     * @param imageId ID изображения.
     * @return Бинарные данные изображения.
     * @throws IOException Если произошла ошибка при чтении файла.
     */
    byte[] getImage(Long imageId) throws IOException;
}