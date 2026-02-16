package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.entity.Ad;

@Mapper(componentModel = "spring")
public interface AdMapper {
    AdMapper INSTANCE = Mappers.getMapper(AdMapper.class);

    @Mapping(target = "image", expression = "java(ad.getImage() != null ? \"/ads/\" + ad.getPk() + \"/image\" : null)")
    AdDTO adToAdDto(Ad ad);

//    Ad adDtoToAd(AdDTO adDTO);

    @Mapping(target = "image", expression = "java(mapImage(ad))")
    default String mapImage(Ad ad) {
        return "/ads/" + ad.getPk() + "/image"; // REST-ссылка для фронта
    }

}