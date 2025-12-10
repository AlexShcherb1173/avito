package ru.skypro.homework.mapper;

import org.mapstruct.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;

@Mapper(
        componentModel = "spring",
        uses = UserMapper.class  // Используем UserMapper для преобразования author
)
public abstract class CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ad", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public abstract CommentEntity toEntity(CreateOrUpdateComment createOrUpdateComment);

    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "authorImage", source = "author.image", qualifiedByName = "imageToUrl")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "createdAt", expression = "java(entity.getCreatedAt().toEpochMilli())")
    @Mapping(target = "pk", source = "id")
    public abstract Comment toDto(CommentEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ad", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public abstract void updateEntityFromDto(CreateOrUpdateComment createOrUpdateComment,
                                             @MappingTarget CommentEntity entity);

}