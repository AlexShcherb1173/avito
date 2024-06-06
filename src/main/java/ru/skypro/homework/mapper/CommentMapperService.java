package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.entity.CommentEntity;

public interface CommentMapperService {
    CommentDto mappingToDto(CommentEntity entity);

    CommentEntity mappingToEntity(CommentDto dto);
}
