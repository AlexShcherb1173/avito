package ru.skypro.homework.mapper;

import org.mapstruct.*;
import ru.skypro.homework.dto.generatedDto.CommentDto;
import ru.skypro.homework.dto.generatedDto.CommentsDto;
import ru.skypro.homework.entity.Avatar;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.Image;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CommentMapper {

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.avatar", target = "authorImage", qualifiedByName = "getImageLink")
    CommentDto commentToCommentDto(Comment comment);

    @Mapping(source = "pk", target = "id")
    @Mapping(source = "author", target = "author.id")
    @Mapping(source = "authorFirstName", target = "author.firstName")
    Comment commentDtoToComment(CommentDto commentDto);

    @Mapping(source = "size", target = "count")
    @Mapping(source = "commentList", target = "results")
    CommentsDto commentListToCommentsDto(Integer size, List<Comment> commentList);

    @Named("getImageLink")
    default String getImageLink(Avatar avatar) {
        return (avatar == null) ? null : "/users/avatar/" + avatar.getId();
    }
}
