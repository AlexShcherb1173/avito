package ru.skypro.homework.mapper;

import org.mapstruct.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.CommentEntity;

/**
 * Маппер для преобразования между {@link CommentEntity} и DTO комментариев.
 */
@Mapper(componentModel = "spring")
public interface CommentMapper {

    /**
     * Преобразует сущность {@link CommentEntity} в DTO {@link Comment}.
     *
     * @param entity сущность комментария
     * @return DTO комментария
     */
    @Mappings({
            @Mapping(target = "author", source = "author.id"),
            @Mapping(target = "authorImage", source = "author.image"),
            @Mapping(target = "authorFirstName", source = "author.firstName"),
            @Mapping(target = "createdAt", expression = "java(entity.getCreatedAt().toEpochMilli())"),
            @Mapping(target = "pk", source = "id")
    })
    Comment toDto(CommentEntity entity);

    /**
     * Создаёт сущность {@link CommentEntity} из DTO {@link CreateOrUpdateComment}.
     * Игнорирует поля id, author, ad и createdAt — они выставляются вручную в сервисе.
     *
     * @param dto DTO с текстом комментария
     * @return новая сущность комментария
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "author", ignore = true),
            @Mapping(target = "ad", ignore = true),
            @Mapping(target = "createdAt", ignore = true)
    })
    CommentEntity fromCreateDto(CreateOrUpdateComment dto);
}
