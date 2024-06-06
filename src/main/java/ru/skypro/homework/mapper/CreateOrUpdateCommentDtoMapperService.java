package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.CommentEntity;

public interface CreateOrUpdateCommentDtoMapperService {
    CommentEntity mappingToEntity(CreateOrUpdateCommentDto dto);

    CreateOrUpdateCommentDto mappingToDto(CommentEntity entity);
}
