package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Comment;

import java.util.List;

/**
 * Mapper for Comment entity (made using mapstruct)
 * Maps CommentDto, CreateOrUpdateCommentDto and List of CommentDto
 */
@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "authorImage", expression = "java(\"/pictures/\" + comment.getAuthor().getImage().getId())")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    CommentDto CommentToCommentDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "ad", ignore = true)
    @Mapping(target = "author", ignore = true)
    Comment CreateOrUpdateCommentDtoToCommentEntity(CreateOrUpdateCommentDto createOrUpdateCommentDto);

    List<CommentDto> CommentListToCommentDtoList(List<Comment> comments);
}