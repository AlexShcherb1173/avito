package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Сервис для управления изображениями.
 * Обеспечивает сохранение, получение и удаление файлов изображений в файловой системе.
 */
public interface ImageService {

    /**
     * Сохраняет изображение на сервере.
     * Генерирует уникальное имя файла на основе UUID.
     * Создает необходимые директории, если они отсутствуют.
     *
     * @param image файл изображения
     * @return уникальное имя сохраненного файла
     * @throws RuntimeException если произошла ошибка ввода-вывода при сохранении
     */
    String saveImage(MultipartFile image);

    /**
     * Получает изображение по имени файла.
     *
     * @param filename имя файла изображения
     * @return массив байтов содержимого изображения
     * @throws RuntimeException если файл не найден или произошла ошибка чтения
     */
    byte[] getImage(String filename);

    /**
     * Удаляет изображение по имени файла.
     * Если файл не существует, операция игнорируется.
     *
     * @param filename имя файла для удаления
     */
    void deleteImage(String filename);
}