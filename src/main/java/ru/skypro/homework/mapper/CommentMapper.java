package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.entity.Comment;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    static LocalDateTime toLocalDate(Long millis) {
        if (millis == null) {
            return null;
        }
        return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    static Long localDateTimeToMillis(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        ZonedDateTime zdt = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();
    }

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author_id", target = "author")
    @Mapping(source = "author_firstName", target = "authorFirstName")
    @Mapping(expression = "java(getUrlToAvatar(comment))", target = "authorImage")
    CommentDto commentToCommentDto(Comment comment);


    @Mapping(source = "pk", target = "id")
    @Mapping(source = "author", target = "author_id")
    @Mapping(source = "authorFirstName", target = "author_firstName")
    Comment commentDtoToComment(CommentDto commentDto);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    void updateComment(CommentDto commentDto, @MappingTarget Comment comment);

    List<CommentDto> commentListToCommentDtoList(List<Comment> comments);

    default CommentsDto listCommentDto(List<Comment> comments) {
        CommentsDto result = new CommentsDto();
        result.setType(comments.size());
        result.setResults(commentListToCommentDtoList(comments));
        return result;
    }

    default String getUrlToAvatar(Comment comment) {
        if (comment.getAuthor() == null) {
            return null;
        }
        return "/users/" + comment.getAuthor().getId() + "/image";
    }
}
