package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.skypro.homework.exception.ImageNotFoundException;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageServiceApi;

@Service
@Log4j2
@RequiredArgsConstructor
public class ImageService implements ImageServiceApi {

    private final ImageRepository imageRepository;

    @Override
    public Image getImageById(Long id) {
        log.info("Получение изображения по идентификатору: {}", id);
        return imageRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Изображение с идентификатором {} не найдено", id);
                    return new ImageNotFoundException("Изображение с идентификатором " + id + " не найдено");
                }
        );
    }

}
