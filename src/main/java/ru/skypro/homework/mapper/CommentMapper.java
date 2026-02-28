package ru.skypro.homework.mapper;

import org.mapstruct.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mappings({
            @Mapping(target = "pk", source = "id"),
            @Mapping(target = "author", source = "author.id"),
            @Mapping(target = "authorFirstName", source = "author.firstName"),
            @Mapping(target = "authorImage", source = "author.image")
    })
    Comment toDto(CommentEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true), // ставим в сервисе
            @Mapping(target = "author", ignore = true),
            @Mapping(target = "ad", ignore = true)
    })
    void applyCreateOrUpdate(CreateOrUpdateComment dto, @MappingTarget CommentEntity entity);

    default Long map(java.time.Instant instant) {
        return instant == null ? null : instant.toEpochMilli();
    }
}
