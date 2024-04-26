package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.rq.comment.Comment;
import ru.skypro.homework.dto.rq.comment.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;

@Mapper(componentModel = "spring")
public interface MapperComment {

    CommentEntity createOrUpdateCommentToEntity(CreateOrUpdateComment createOrUpdateComment);

    @Mapping(target = "author", expression = "java(commentEntity.getAuthor().getId())")
    @Mapping(target = "authorImage", expression = "java(commentEntity.getAuthor().getImage())")
    @Mapping(target = "authorFirstName", expression = "java(commentEntity.getAuthor().getFirstName())")
    Comment entityToComment(CommentEntity commentEntity);
}
