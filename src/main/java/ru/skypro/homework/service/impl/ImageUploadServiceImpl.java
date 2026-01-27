package ru.skypro.homework.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.service.ImageUploadService;

/**
 * Класс, создающий логику по выгрузке любых картинок из файловой системы
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ImageUploadServiceImpl implements ImageUploadService {


    // Первый вариант (используется в UserController)
    @Override
    public byte[] getImageForUserOrAd(String urlPath) {

        Path path = Path.of("images/" + urlPath);
        // В конструктор объекта типа Path передаем путь к файлу, состоящий из имени папки, "/" и имени файла,
        // извлеченного из сущностей Ad или User

        byte[] image;
        try (InputStream is = Files.newInputStream(path);
             ByteArrayOutputStream os = new ByteArrayOutputStream(1024)) {

            is.transferTo(os);
            image = os.toByteArray();
            log.info("File with path = {} uploaded successfully!", path);

        } catch (IOException ex) {
            if (ex instanceof NoSuchFileException) {
                throw new NotFoundException("File for entity with path: " + path + " not founded");
            } else {
                log.error("Error downloading image file for ad with path = {}", path, ex);
                throw new RuntimeException(ex.getMessage());
            }

        }
        return image;
    }


    // Второй вариант (используется в AdController)
    @Override
    public Resource getImageForUserOrAd2(String urlPath) {

        Path path2 = Path.of("images/" + urlPath);
        // В конструктор объекта типа Path передаем путь к файлу, состоящий из имени папки, "/" и имени файла,
        // извлеченного из сущностей Ad или User

        Resource resource;
        try {
            resource = new UrlResource(path2.toUri());
            log.info("File with path2 = {} uploaded successfully!", path2);
        } catch (IOException ex) {
            log.error("Error downloading image file for ad with path = {}", path2, ex);
            throw new RuntimeException(ex.getMessage());
        }
        return resource;
    }

    // Интерфейс org.springframework.core.io.Resource является фундаментальным компонентом фреймворка Spring и
    // представляет собой абстракцию для работы с ресурсами различных типов. Он обеспечивает единый способ доступа к
    // файлам, URL-адресам и другим ресурсам в приложении независимо от их физического расположения или типа.

    // Основные функции интерфейса Resource:
    // 1. Проверка существования и доступности ресурса:
    // exists() - проверяет физическое существование ресурса
    // isReadable() — определяет возможность чтения содержимого ресурса
    // isOpen() — указывает на наличие открытой потоковой передачи данных
    // 2. Работа с ресурсом через различные представления:
    // getURL() — получение URL-адреса ресурса
    // getURI() - получение URI ресурса
    // getFile() — преобразование в объект File файловой системы
    // 3. Чтение содержимого ресурса:
    // getContentAsByteArray() — чтение содержимого в виде массива байтов
    // getContentAsString(Charset) — чтение содержимого в виде строки с указанным кодированием
    // readableChannel() — предоставление канала для чтения данных
    // 4. Информационные методы:
    // getDescription() — получение описательной информации о ресурсе
    // getFilename() — получение имени файла (если применимо)
    // contentLength() - определение размера ресурса
    // lastModified() — получение временной метки последнего изменения

    // Пример использования Resource в Spring-приложении:
    // Resource resource = new ClassPathResource("/config.properties");
    // if (resource.exists()) {
    //    String content = resource.getContentAsString(StandardCharsets.UTF_8);
    //    // Использование содержимого ресурса
    // }

    // Важные особенности использования Resource:
    // - Интерфейс является частью ядра Spring Framework и повсеместно используется в различных компонентах фреймворка
    // - Предоставляет единый способ работы с ресурсами независимо от их расположения (файловая система, классы, URL и т.д.)
    // - Реализует паттерн Strategy для работы с различными типами ресурсов
    // - Обеспечивает безопасную обработку ошибок через исключения IOException при работе с физическими ресурсами
    // Это позволяет разработчикам писать код, который работает одинаково хорошо с разными типами ресурсов, не беспокоясь
    // о конкретной реализации доступа к ним.

    // Интерфейс InputStreamSource является базовым компонентом Spring Framework и выполняет фундаментальную функцию:
    // он предоставляет единственный метод getInputStream(), который возвращает InputStream для чтения содержимого ресурса.
    // Данный интерфейс наследует (расширяет) интерфейс Resource.

    // Базовые компоненты
    // InputStreamSource — базовый интерфейс:
    // - Предоставляет единственный метод getInputStream()
    // - Является точкой входа для чтения данных из любого источника
    // Ресурс — расширенный интерфейс:
    // - Расширяет возможности InputStreamSource за счёт дополнительной функциональности
    // - Добавляет методы для проверки существования и доступности ресурса
    // - Обеспечивает работу с URL, URI и файловой системой

    // Абстрактные классы
    // AbstractResource:
    // - Реализует базовую логику проверки ресурса
    // - Предоставляет реализации методов exists(), isReadable() и isOpen()
    // - Служит основой для простой реализации ресурсов
    // AbstractFileResolvingResource:
    // - Добавляет функциональность для работы с файловой системой
    // - Реализует методы для получения URL, URI и файловых объектов
    // - Используется для ресурсов, связанных с файловой системой

    // Конкретные реализации
    // ByteArrayResource:
    // - Работает с данными в виде байтового массива
    // - Наследуется от AbstractResource
    // - Подходит для небольших объёмов данных в памяти
    // ClassPathResource:
    // - Обеспечивает доступ к ресурсам из пути к классам
    // - Наследуется от AbstractFileResolvingResource
    // - Широко используется для конфигурационных файлов
    // FileSystemResource:
    // - Работает с файлами в файловой системе
    // - Наследуется от AbstractFileResolvingResource
    // - Подходит для работы с физическими файлами на диске

    // Пример совместного использования этих компонентов:
    // Resource resource = null;
    //
    // // Работа с байтовым массивом
    // resource = new ByteArrayResource("Hello World".getBytes());
    // try (InputStream inputStream = resource.getInputStream()) {
    //    // Чтение данных
    // }
    //
    // // Работа с файлом из classpath
    // resource = new ClassPathResource("/config.properties");
    // if (resource.exists()) {
    //    String content = resource.getContentAsString(StandardCharsets.UTF_8);
    //    // Использование содержимого
    // }
    //
    // // Работа с файлом файловой системы
    // resource = new FileSystemResource("/path/to/file.txt");
    // try {
    //    File

}
