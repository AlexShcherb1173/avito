package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import ru.skypro.homework.entity.Image;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    @Named("imageToPath")
    default String imageToPath(Image image) {
        return image != null ? "/images/" + image.getId() : null;
    }
}