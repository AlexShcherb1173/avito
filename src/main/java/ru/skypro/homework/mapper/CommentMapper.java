package ru.skypro.homework.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.responseDto.CommentDto;

import java.time.ZoneOffset;
import java.util.List;

// Маппер для преобразования между сущностью Comment и CommentDto.
// Обеспечивает корректное отображение полей комментариев.

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    /**
     * Преобразует сущность Comment в CommentDto.
     * Обратите внимание:
     * - id → pk
     * - author.id → author
     * - author.firstName → authorFirstName
     * - author.image → authorImage (с префиксом)
     * - createdAt → toEpochSecond в UTC
     */
    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.image", target = "authorImage", qualifiedByName = "addImagePrefix")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(source = "text", target = "text")
    CommentDto toCommentDto(Comment comment);

    // Преобразует список комментариев в список CommentDto.
    // @param comments список сущностей комментариев
    // @return список DTO

    List<CommentDto> toCommentDtoList(List<Comment> comments);

    // Выполняется после маппинга для установки временной метки создания.
    // @param comment сущность комментария
    // @param dto целевой DTO

    @AfterMapping
    default void mapCreatedAt(Comment comment, @MappingTarget CommentDto dto) {
        if (comment.getCreatedAt() != null) {
            dto.setCreatedAt(comment.getCreatedAt().toEpochSecond(ZoneOffset.UTC));
        }
    }

    // Преобразует имя файла изображения в полный URL.
    // Если изображение уже содержит полный путь, возвращает как есть.
    // @param image путь или имя файла изображения
    // @return полный URL изображения

    @Named("addImagePrefix")
    default String addImagePrefix(String image) {
        if (image == null || image.isBlank()) {
            return null;
        }

        // Если уже полный URL, возвращаем как есть
        if (image.startsWith("/images/")) {
            return image;
        }

        // Если это просто имя файла, добавляем префикс пути
        return "/images/users/" + image;
    }
}