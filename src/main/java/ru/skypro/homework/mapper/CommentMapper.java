package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CommentsDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toEntity(CommentsDTO dto);
    CommentsDTO toDTO(Comment comment);

}
