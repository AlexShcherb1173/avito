package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface AdMapper {

    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "image", expression = "java(ad.getImage() != null ? \"/images/\" + ad.getImage() : null)")
    AdDTO adToAdDTO(Ad ad);

    default Integer mapAuthorId(User user) {
        return user != null ? user.getId() : null;
    }

    @Mapping(target = "author", ignore = true)
    Ad adDTOToAd(AdDTO adDTO);

    default Ad setAuthor(Ad ad, User author) {
        if (ad != null && author != null) {
            ad.setAuthor(author);
        }
        return ad;
    }
}
