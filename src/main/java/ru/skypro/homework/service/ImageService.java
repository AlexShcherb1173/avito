package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * Сервисный интерфейс для работы с изображениями.
 *
 * Интерфейс предоставляет методы для сохранения и получения изображений:
 * - Сохранение изображения {@link #saveImage(MultipartFile)}. Метод сохраняет файл и возвращает его имя.
 * - Получение байтов изображения {@link #getImageBytes(String)}. Метод принимает имя файла и возвращает его содержимое в виде массива байтов.
 *
 * Методы могут выбрасывать исключение {@link IOException}, если произошла ошибка при работе с файлами.
 */
public interface ImageService {

    String saveImage(MultipartFile file) throws IOException;

    byte[] getImageBytes(String filename) throws IOException;
}
