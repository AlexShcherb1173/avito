package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    CommentEntity toComment(CommentDTO commentDTO);
    CommentEntity toComment(CreateOrUpdateComment createdComment);
    CommentDTO toDto(CommentEntity commentEntity);
    CreateOrUpdateComment toCreatedComment(CommentEntity commentEntity);
}