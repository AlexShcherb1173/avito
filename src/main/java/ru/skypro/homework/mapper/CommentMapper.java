package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.skypro.homework.dto.CreateOrUpdateComment;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    // ENTITY → DTO
    @Mappings({
            @Mapping(target = "pk", expression = "java(entity.getId() == null ? null : entity.getId().intValue())"),
            @Mapping(target = "author", expression = "java(entity.getAuthor() == null ? null : entity.getAuthor().getId().intValue())"),
            @Mapping(target = "authorImage", source = "author.image"),
            @Mapping(target = "authorFirstName", source = "author.firstName"),
            @Mapping(target = "createdAt", source = "createdAt")
    })
    ru.skypro.homework.dto.Comment toDto(ru.skypro.homework.entity.Comment entity);


    // DTO → ENTITY
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "ad", ignore = true),
            @Mapping(target = "author", ignore = true),
            @Mapping(target = "createdAt", ignore = true)
    })
    ru.skypro.homework.entity.Comment toEntity(CreateOrUpdateComment dto);


    // КОНВЕРТАЦИЯ LocalDateTime → Long (millis)
    default Long map(LocalDateTime value) {
        if (value == null) {
            return null;
        }
        return value.atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }
}