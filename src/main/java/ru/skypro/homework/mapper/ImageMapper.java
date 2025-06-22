package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.ImageDto;
import ru.skypro.homework.model.Image;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    @Mapping(source = "ad.pk", target = "adId")
    @Mapping(source = "user.id", target = "userId")
    ImageDto imageToImageDTO(Image image);

    @Mapping(source = "adId", target = "ad.pk")
    @Mapping(source = "userId", target = "user.id")
    Image imageDTOToImage(ImageDto imageDTO);
}
