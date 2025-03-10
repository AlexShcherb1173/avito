package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;

/**
 * Интерфейс, использующий библиотеку MapStruct для автоматического преобразования между объектами типа {@link Ad} и {@link AdDTO}.
 * Этот интерфейс выполняет маппинг данных, таких как автор объявления и изображение, а также преобразует объект {@link AdDTO} в {@link Ad}.
 * Использует {@link UserMapper} для работы с маппингом данных пользователя, связанного с объявлением.
 * MapStruct автоматически генерирует реализацию этого интерфейса для эффективного преобразования между объектами.
 */
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
