package ru.skypro.homework.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import org.mapstruct.Mapper;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Advertisement;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

//    @Mapping(target = "author", source = "entity.author.id") // Маппим ID автора
//    @Mapping(target = "createdAt", expression = "java(mapLocalDateTimeToLong(entity.getCreatedAt()))") // Преобразуем дату
//    @Mapping(target = "authorFirstName", source = "entity.author.firstName") // Имя автора
//    @Mapping(target = "authorImage", expression = "java(entity.getAuthor().getUserAvatar() != null ? entity.getAuthor().getUserAvatar().getFilePath() : null)") // Аватар автора (если есть)
//    Comment commentEntityToCommentDTO(CommentEntity entity);
//
//    default long mapLocalDateTimeToLong(LocalDateTime localDateTime) {
//        return localDateTime.toEpochSecond(ZoneOffset.UTC);
//    }
//    @Mapping(target = "text", source = "comment.text")
//    CreateOrUpdateComment toCreateOrUpdateComment(Comment comment);
@Mapping(source = "author.id", target = "author")
@Mapping(source = "author.userAvatar.filePath", target = "authorImage")
@Mapping(source = "author.firstName", target = "authorFirstName")
@Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "mapLocalDateTimeToLong")
Comment toDto(CommentEntity commentEntity);

    @Mapping(target = "pk", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "author", source = "user")
    @Mapping(target = "advertisement", source = "advertisement")
    CommentEntity toEntity(CreateOrUpdateComment commentDto, User user, Advertisement advertisement);

    @Named("mapLocalDateTimeToLong")
    default long mapLocalDateTimeToLong(LocalDateTime dateTime) {
        return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
