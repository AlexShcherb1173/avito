package ru.skypro.homework.mapper.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.mapper.CreateOrUpdateCommentDtoMapperService;

@Service
public class CreateOrUpdateCommentMapperServiceImpl implements CreateOrUpdateCommentDtoMapperService {
    @Override
    public CommentEntity mappingToEntity(CreateOrUpdateCommentDto dto) {
        CommentEntity entity = new CommentEntity();
        entity.setText(dto.getText());
        return entity;
    }

    @Override
    public CreateOrUpdateCommentDto mappingToDto(CommentEntity entity) {
        CreateOrUpdateCommentDto dto = new CreateOrUpdateCommentDto();
        dto.setText(entity.getText());
        return dto;
    }
}
