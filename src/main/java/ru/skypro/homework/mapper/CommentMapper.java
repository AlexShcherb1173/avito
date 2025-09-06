package ru.skypro.homework.mapper;

import org.mapstruct.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.CommentEntity;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mappings({
            @Mapping(target = "author", source = "author.id"),
            @Mapping(target = "authorImage", source = "author.image"),
            @Mapping(target = "authorFirstName", source = "author.firstName"),
            @Mapping(target = "createdAt", expression = "java(entity.getCreatedAt().toEpochMilli())"),
            @Mapping(target = "pk", source = "id")
    })
    Comment toDto(CommentEntity entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "author", ignore = true),
            @Mapping(target = "ad", ignore = true),
            @Mapping(target = "createdAt", ignore = true)
    })
    CommentEntity fromCreateDto(CreateOrUpdateComment dto);
}
