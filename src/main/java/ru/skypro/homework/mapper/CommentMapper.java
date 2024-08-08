package ru.skypro.homework.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "user.id", target = "author")
    @Mapping(source = "user.firstName", target = "authorFirstName")
    @Mapping(target = "authorImage", expression = "java(\"/image/\" + user.getImage().getId())")
    @Mapping(source = "comment.id", target = "pk")
    CommentDto toCommentDTO (Comment comment, User user);
}
