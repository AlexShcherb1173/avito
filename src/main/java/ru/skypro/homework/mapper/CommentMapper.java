package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.Comment;
import java.util.List;


@Mapper(componentModel = "spring")
public interface CommentMapper
{

    @Mapping(target = "authorImage", source = "user.avatar.url")
    @Mapping(source = "id", target = "pk")
    @Mapping(source = "user.id", target = "author")
    @Mapping(source = "user.firstName", target = "authorFirstName")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd HH:mm")

    CommentDto toDTO(Comment comment);

    Comments toCommentsDTO(Integer count, List<Comment> results);
    CreateOrUpdateComment toDTO3(Comment comment);


}
