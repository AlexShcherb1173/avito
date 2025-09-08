package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ad", ignore = true)
    CommentEntity toEntity(CreateOrUpdateComment createOrUpdateComment);

    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "authorImage", source = "author.imagePath", qualifiedByName = "imagePathToImage")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    Comment toDto(CommentEntity commentEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ad", ignore = true)
    void updateEntityFromDto(CreateOrUpdateComment createOrUpdateComment, @org.mapstruct.MappingTarget CommentEntity commentEntity);

    @org.mapstruct.Named("imagePathToImage")
    default String imagePathToImage(String imagePath) {
        return imagePath != null ? "/users/image/" + imagePath : null;
    }
}