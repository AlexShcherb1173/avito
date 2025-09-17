package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.comments.CommentCreateOrUpdateRequest;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentDto toCommentDto(Comment entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "ad", expression = "java(ad)")
    Comment toComment(CommentCreateOrUpdateRequest request, Ad ad);
}
