package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.model.Ad;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ExtendedAdMapper {

    @Mapping(target = "image", expression = "java(ad.getImage() != null ? \"/images/\" + ad.getImage() : null)")
    ExtendedAdDTO adToExtendedAdDTO(Ad ad);

    @Mapping(target = "author", ignore = true)
    Ad extendedAdDTOtoAd(ExtendedAdDTO extendedAdDTO);

}