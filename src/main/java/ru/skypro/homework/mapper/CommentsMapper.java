package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.CommentEntity;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentsMapper {

    @Mappings({
            @Mapping(target = "pk", source = "id"),
            @Mapping(target = "author", source = "author.id"),
            @Mapping(target = "authorImage", source = "author.avatar.path"),
            @Mapping(target = "authorFirstName", source = "author.firstName"),
            @Mapping(target = "createdAt", source = "createdAt.year")
    })
    CommentDto commentToCommentDTO(CommentEntity comment);


    List<CommentDto> commentsToCommentsDTO(List<CommentEntity> comments);
    CreateOrUpdateCommentDto commentToCreateOrUpdateCommentDto(CommentEntity comment);

}
