package ru.skypro.homework.mapper;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mapstruct.Mapper;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.model.CommentModel;

@Mapper(componentModel = "spring", uses = {UserMapper.class, AdMapper.class})
public interface CommentMapper {

    @Schema(description = "Преобразование объекта модели Comment в объект DTO Comment")
    CommentModel toDTO(Comment model);

    @Schema(description = "Преобразование объекта DTO Comment в объект модели Comment")
    Comment toModel(CommentModel dto);
}
