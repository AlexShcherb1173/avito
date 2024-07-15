package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;
import ru.skypro.homework.dto.comments.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Comment;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "author.image.filePath", target = "authorImage")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    CommentDto commentToCommentDTO(Comment comment);

    @Mapping(source = "author", target = "author.id")
    @Mapping(source = "pk", target = "id")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "ad", ignore = true)
    Comment commentDTOToComment(CommentDto commentDTO);

    List<CommentDto> commentsToCommentDTOs(List<Comment> comments);

    List<Comment> commentDTOsToComments(List<CommentDto> commentDtos);

    @Mapping(target = "results", source = "comments")
    @Mapping(expression = "java(comments.size())", target = "count")
    default CommentsDto commentsToCommentsDTO(List<Comment> comments) {
        CommentsDto commentsDTO = new CommentsDto();
        commentsDTO.setResults(commentsToCommentDTOs(comments));
        commentsDTO.setCount(comments.size());
        return commentsDTO;
    }

    CreateOrUpdateCommentDto commentToCreateOrUpdateCommentDTO(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java((int) (System.currentTimeMillis() / 1000))")
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ad", ignore = true)
    Comment createOrUpdateCommentDTOToComment(CreateOrUpdateCommentDto createOrUpdateCommentDTO);
}
