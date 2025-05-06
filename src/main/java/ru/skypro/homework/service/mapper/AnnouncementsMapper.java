package ru.skypro.homework.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.AnnouncementsDTO;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.model.Announcemenst;
import ru.skypro.homework.model.User;

@Mapper
public interface AnnouncementsMapper {
    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );
    public AnnouncementsDTO announcementsDTO(Announcemenst announcemenst);
    public Announcemenst announcemenst(AnnouncementsDTO announcementsDTO);

}
