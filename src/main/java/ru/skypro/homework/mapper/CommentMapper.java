package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ad", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    CommentEntity toEntity(CreateOrUpdateComment createOrUpdateComment);

    @Mapping(target = "author", source = "entity.author.id")
    @Mapping(target = "authorImage", expression = "java(entity.getAuthor().getImage() != null ? \"/images/\" + entity.getAuthor().getImage() : null)")
    @Mapping(target = "authorFirstName", source = "entity.author.firstName")
    @Mapping(target = "createdAt", expression = "java(entity.getCreatedAt().toEpochMilli())")
    @Mapping(target = "pk", source = "entity.id")
    @Mapping(target = "text", source = "entity.text")
    Comment toDto(CommentEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ad", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(CreateOrUpdateComment createOrUpdateComment, @org.mapstruct.MappingTarget CommentEntity entity);
}