package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.comments.CommentDTO;
import ru.skypro.homework.dto.comments.CommentsDTO;
import ru.skypro.homework.dto.comments.CreateOrUpdateCommentDTO;
import ru.skypro.homework.model.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);
    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "author.image", target = "authorImage")
    @Mapping(source = "author.name", target = "authorFirstName")
    CommentDTO commentToCommentDTO(Comment comment);

    @Mapping(source = "author", target = "author.id")
    @Mapping(source = "pk", target = "id")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "ad", ignore = true)
    Comment commentDTOToComment(CommentDTO commentDTO);

    List<CommentDTO> commentsToCommentDTOs(List<Comment> comments);

    List<Comment> commentDTOsToComments(List<CommentDTO> commentDTOs);

    @Mapping(source = "comments", target = "results")
    @Mapping(expression = "java(comments.size())", target = "count")
    CommentsDTO commentsToCommentsDTO(List<Comment> comments);

    CreateOrUpdateCommentDTO commentToCreateOrUpdateCommentDTO(Comment comment);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java((int) (System.currentTimeMillis() / 1000))")
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ad", ignore = true)
    Comment CreateOrUpdateCommentDTOToComment(CreateOrUpdateCommentDTO createOrUpdateCommentDTO);
}
