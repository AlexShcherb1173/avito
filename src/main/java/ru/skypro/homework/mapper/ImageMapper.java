package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.image.ImageDTO;
import ru.skypro.homework.entity.Image;

@Component
public class ImageMapper {
    public ImageDTO mapToDo (Image image) {
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setId(imageDTO.getId());
        imageDTO.setMediaType(image.getMediaType());
        imageDTO.setFileSize(image.getFileSize());

        return imageDTO;
    }
}
