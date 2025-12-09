package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;

/**
 * Маппер для преобразования между сущностью {@link CommentEntity} и DTO комментариев.
 * Использует MapStruct для автоматической генерации кода преобразования.
 */
@Mapper(componentModel = "spring")
public interface CommentMapper {

    /**
     * Преобразует объект {@link CreateOrUpdateComment} в сущность {@link CommentEntity}.
     * Игнорирует поля, которые устанавливаются отдельно.
     *
     * @param createOrUpdateComment DTO с данными комментария
     * @return сущность комментария
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ad", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    CommentEntity toEntity(CreateOrUpdateComment createOrUpdateComment);

    /**
     * Преобразует сущность {@link CommentEntity} в DTO {@link Comment}.
     * Формирует URL для изображения автора и преобразует дату создания.
     *
     * @param entity сущность комментария
     * @return DTO комментария
     */
    @Mapping(target = "author", source = "entity.author.id")
    @Mapping(target = "authorImage", expression = "java(getImageUrl(entity.getAuthor().getImage()))")
    @Mapping(target = "authorFirstName", source = "entity.author.firstName")
    @Mapping(target = "createdAt", expression = "java(entity.getCreatedAt().toEpochMilli())")
    @Mapping(target = "pk", source = "entity.id")
    @Mapping(target = "text", source = "entity.text")
    Comment toDto(CommentEntity entity);

    /**
     * Обновляет сущность {@link CommentEntity} данными из {@link CreateOrUpdateComment}.
     * Обновляются только предоставленные поля.
     *
     * @param createOrUpdateComment DTO с обновляемыми данными
     * @param entity сущность для обновления
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ad", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(CreateOrUpdateComment createOrUpdateComment, @org.mapstruct.MappingTarget CommentEntity entity);

    /**
     * Формирует URL для изображения пользователя.
     * Добавляет временную метку для предотвращения кэширования.
     *
     * @param image имя файла изображения
     * @return строка с URL изображения
     */
//    default String getImageUrl(String image) {
//        if (image == null || image.isEmpty()) {
//            return null;
//        }
//        // Добавляем временную метку для предотвращения кэширования
//        long timestamp = System.currentTimeMillis();
//        return "/images/" + image + "?v=" + timestamp;
//    }
}